package com.example.cep.data.search.datasource.remote;


import com.example.cep.domain.model.Cep;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepService {

    @GET("{id}/json")
    Call<Cep> select(@Path("id") String id);

}
