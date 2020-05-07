package com.example.proficiencyexercise.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import io.realm.RealmObject

open class Fact(
    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("description")
    @Expose
    var description: String? = null,

    @SerializedName("imageHref")
    @Expose
    var imageHref: String? = null
) : RealmObject()