package com.br.leanlife.models;

import java.util.List;

/**
 * Created by Raphael on 21/05/2016.
 */
public class Chat {
    public int id;
    public int tipo;
    public String mensagem;

    public Chat() {
    }

    public Chat(int id,int tipo,String mensagem) {
        this.id = id;
        this.tipo = tipo;
        this.mensagem = mensagem;
    }

}
