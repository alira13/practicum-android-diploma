package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName

data class VacanciesResponse(
    val items: List<ItemDto>,
    val found: Long,
    val page: Long,
    val pages: Long,
    @SerializedName("per_page")
    val perPage: Long,
    val clusters: List<Cluster>,
    val arguments: List<Argument>,
    @SerializedName("alternate_url")
    val alternateUrl: String?,
    val fixes: Fixes,
    val suggests: Suggests
) : Response()

data class Cluster(
    val id: String,
    val items: List<SearchCluster>,
    val name: String
)

data class Argument(
    val argument: String,
    @SerializedName("cluster_group")
    val clusterGroup: ClusterGroup,
    @SerializedName("disable_url")
    val disableUrl: String,
    @SerializedName("hex_color")
    val hexColor: String?,
    @SerializedName("metro_type")
    val metroType: String?,
    val name: String?,
    val value: String,
    @SerializedName("value_description")
    val valueDescription: String?
)

data class ClusterGroup(
    val id: String,
    val name: String
)

data class SearchCluster(
    val count: Long,
    val name: String,
    val type: String,
    val url: String
)

data class Fixes(
    val fixed: String,
    val original: String
)
data class Suggests(
    val found: Int,
    val value: String
)

