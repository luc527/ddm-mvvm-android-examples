package com.example.cep.presentation.search;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.cep.BR;
import com.example.cep.data.common.util.Result;
import com.example.cep.data.common.util.RetrofitInitializer;
import com.example.cep.data.search.datasource.remote.CepService;
import com.example.cep.domain.model.Cep;
import com.example.cep.domain.usecase.search.SearchCepUseCase;

public class SearchCepViewModel extends BaseObservable {

    private SearchCepUseCase useCase;
    private String cepDigitado = "";
    private Cep cep;
    private String erro;

    public SearchCepViewModel() {
    }

    public SearchCepViewModel setUseCase(SearchCepUseCase useCase) {
        this.useCase = useCase;
        return this;
    }

    @Bindable
    public String getCepDigitado() {
        return cepDigitado;
    }

    public void setCepDigitado(String cepDigitado) {
        this.cepDigitado = cepDigitado;
        notifyPropertyChanged(BR.cepDigitado);
        notifyPropertyChanged(BR.formatoCepValido);
    }

    @Bindable
    public String getLogradouro() {
        return cep == null ? "" : cep.getLogradouro();
    }

    @Bindable
    public boolean getFormatoCepValido() {
        return cepDigitado != null && cepDigitado.length() == 8;
    }

    @Bindable
    public String getErro() {
        return this.erro;
    }

    @Bindable
    public String getBairro() {
        return cep == null ? "" : cep.getBairro();
    }

    @Bindable
    public String getCidade() {
        return cep == null ? "" : (cep.getLocalidade() + " - " + cep.getUf());
    }

    public void setErro(String erro) {
        this.erro = erro;
        notifyPropertyChanged(BR.erro);
    }

    public void setCep(Cep cep) {
        this.cep = cep;
        notifyPropertyChanged(BR.logradouro);
        notifyPropertyChanged(BR.bairro);
        notifyPropertyChanged(BR.cidade);
        notifyPropertyChanged(BR.cep);
    }

    @Bindable
    public String getCep() {
        return cep == null ? "" : cep.getCep();
    }

    public void buscarCep(View view) {
        setErro(null);

        Handler handler = new Handler();

        CepService cepService = new RetrofitInitializer().getCep();

        Result<Cep> result = useCase.execute(this.cepDigitado);

        if (result.hasError()) {
            handler.post(() -> Toast.makeText(view.getContext(), "Error: (" + result.getError().getMessage() + ")", Toast.LENGTH_LONG).show());
        } else if (result.getResponse() != null) {
            Cep cep = result.getResponse();
            if (cep.getErro()) {
                setErro("CEP inválido");
            } else {
                setCep(cep);
            }
        }
    }
}
