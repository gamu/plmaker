package ru.gamu.playlistmaker.features.search

/*class SearchRequestHandler  {
    private val searchService: ISearchService

    init {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        searchService = retrofit.create(ISearchService::class.java)
    }

    fun makeCall(searchToken: String, success: (values: List<Track>?) -> Unit) {
        val formatter = SimpleDateFormat("mm:ss", Locale.getDefault()).format(293000L)
        searchService.search(searchToken).enqueue(object: Callback<ResponseRoot> {
            override fun onResponse(call: Call<ResponseRoot>, response: Response<ResponseRoot>) {
                val items = response.body()?.results?.let {
                    val mappedResult = mutableListOf<Track>()
                    for (item in it) {
                        if(item.trackName != null){
                            mappedResult.add(Track(
                                item.trackName,
                                item.artistName,
                                formatter.format(item.trackTimeMillis),
                                item.artworkUrl100)
                            )
                        }
                    }
                    mappedResult
                }
                success(items)
            }

            override fun onFailure(call: Call<ResponseRoot>, t: Throwable) {
                success(null)
            }
        })
    }

    companion object {
        val baseUrl = "https://itunes.apple.com"
    }
}*/