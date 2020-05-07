package com.example.proficiencyexercise.ui.fact

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.proficiencyexercise.model.About
import com.example.proficiencyexercise.network.FactApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class FactListViewModel(
    private val mFactApiInterface: FactApiInterface = FactApiInterface.create(),
    private val mRealm: Realm = Realm.getDefaultInstance()
) : ViewModel(), RealmChangeListener<RealmResults<About>> {

    private var disposable = CompositeDisposable()

    val about = MutableLiveData<About>()
    val error = MutableLiveData<Throwable>()
    val loading = MutableLiveData<Boolean>()

    override fun onChange(results: RealmResults<About>) {
        if (results.isNotEmpty()) {
            about.postValue(results[0])
        }
    }

    private val mQuery = mRealm.where(About::class.java).findAll()

    init {
        mQuery?.addChangeListener(this)
        loadFacts()
    }

    fun loadFacts() {
        disposable.add(
            mFactApiInterface
                .getFacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loading.postValue(true) }
                .doOnTerminate { loading.postValue(false) }
                .subscribe(
                    {
                        mRealm.executeTransaction { transaction ->
                            transaction.delete(About::class.java)
                            transaction.insert(it)
                        }
                    },
                    {
                        error.postValue(it)
                    })
        )
    }

    override fun onCleared() {
        super.onCleared()
        mQuery?.removeChangeListener(this)
        disposable.dispose()
    }
}