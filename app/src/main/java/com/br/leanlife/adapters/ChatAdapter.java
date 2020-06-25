package com.br.leanlife.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.br.leanlife.ExercicioActivity;
import com.br.leanlife.FuncoesBasicas;
import com.br.leanlife.R;
import com.br.leanlife.models.Chat;
import com.br.leanlife.models.Exercicio;
import com.br.leanlife.models.Treino;

import java.util.List;
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<Chat> msgList;
    private Activity mContext;
    private FuncoesBasicas fb = new FuncoesBasicas();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mensagem,mensagem2,mensagembase;
        public LinearLayout div,div2,div3;
        public MyViewHolder(View view) {
            super(view);
            mensagem = (TextView) view.findViewById(R.id.message_text);
            mensagem2 = (TextView) view.findViewById(R.id.message_text2);
            mensagembase = (TextView) view.findViewById(R.id.chatmesage1);
            div = (LinearLayout) view.findViewById(R.id.bubble_layout);
            div2 = (LinearLayout) view.findViewById(R.id.bubble_layout2);
            div3 = (LinearLayout) view.findViewById(R.id.bubble_layout3);
            div.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //edita

                }
            });
        }
    }


    public ChatAdapter(List<Chat> msgList,Activity context) {
        this.msgList = msgList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_chat, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chat emp = msgList.get(position);

        SharedPreferences preferences =
                mContext.getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String language = preferences.getString("language", "");

        holder.div.setTag(emp.id);
        holder.div2.setTag(emp.id);
        if(emp.tipo == 0)
        {
            //chatbot
            holder.div.setVisibility(View.GONE);
            holder.div3.setVisibility(View.GONE);
            holder.div2.setVisibility(View.VISIBLE);
            holder.mensagem2.setText(emp.mensagem);
        }
        else if(emp.tipo == -1)
        {
            holder.div2.setVisibility(View.GONE);
            holder.div.setVisibility(View.GONE);
            holder.div3.setVisibility(View.VISIBLE);
            if(emp.mensagem.equals("x"))
            {
                holder.mensagembase.setVisibility(View.GONE);
            }
        }
        else
        {
            //pessoa
            holder.div2.setVisibility(View.GONE);
            holder.div3.setVisibility(View.GONE);
            holder.div.setVisibility(View.VISIBLE);
            holder.mensagem.setText(emp.mensagem);
        }

    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }
}
