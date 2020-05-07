package com.example.proficiencyexercise.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject

open class About(
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("rows")
    @Expose
    var facts: RealmList<Fact> = RealmList()
) : RealmObject()