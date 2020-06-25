package com.br.leanlife.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.br.leanlife.models.Exercicio;
import com.br.leanlife.models.Treino;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;
public class TreinoAdapter extends RecyclerView.Adapter<TreinoAdapter.MyViewHolder> {
    private List<Exercicio> msgList;
    private Activity mContext;
    private String ulttreinoclick,podemodificar,abreespecial;
    private FuncoesBasicas fb = new FuncoesBasicas();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, descricao, descricao2;
        public ImageView img1;
        public RelativeLayout div;
        public MyViewHolder(View view) {
            super(view);
            titulo = (TextView) view.findViewById(R.id.titulo);
            descricao = (TextView) view.findViewById(R.id.descricao);
            descricao2 = (TextView) view.findViewById(R.id.descricao2);
            img1 = (ImageView) view.findViewById(R.id.img1);
            div = (RelativeLayout) view.findViewById(R.id.div);
            div.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(mContext, ExercicioActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("ulttreinoclick", ulttreinoclick);
                    mBundle.putString("podemodificar", podemodificar);
                    mBundle.putString("abreespecial", abreespecial);
                    mBundle.putString("idexeroutine", ""+div.getTag());
                    myIntent.putExtras(mBundle);
                    mContext.startActivity(myIntent);
                }
            });
        }
    }


    public TreinoAdapter(List<Exercicio> msgList,Activity context,String ulttreinoclick, String podemodificar, String abreespecial) {
        this.msgList = msgList;
        this.mContext = context;
        this.ulttreinoclick = ulttreinoclick;
        this.podemodificar = podemodificar;
        this.abreespecial = abreespecial;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.container_treino, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Exercicio emp = msgList.get(position);

        SharedPreferences preferences =
                mContext.getSharedPreferences("com.br.leanlife", Context.MODE_PRIVATE);
        String language = preferences.getString("language", "");
        if(language.equals("1"))
        {
            //en
            holder.descricao.setText(emp.exercise_id__exercise_routine.name_english);
            holder.titulo.setText(emp.group_exercise_id__exercise_routine.muscular_group_id__group_exercise.name_english);
        }
        else if(language.equals("2"))
        {
            //es
            holder.descricao.setText(emp.exercise_id__exercise_routine.name_espanish);
            holder.titulo.setText(emp.group_exercise_id__exercise_routine.muscular_group_id__group_exercise.name_espanish);
        }
        else
        {
            //pt
            holder.descricao.setText(emp.exercise_id__exercise_routine.name_port);
            holder.titulo.setText(emp.group_exercise_id__exercise_routine.muscular_group_id__group_exercise.name_port);
        }

        holder.div.setTag(emp.id);
        holder.descricao2.setText("");
        if(emp.flag_status == 1)
        {
            holder.img1.setImageResource(R.drawable.treino_past);
            holder.descricao2.setText(mContext.getString(R.string.exercicios_2));
        }
        else if(emp.flag_status == 0 || emp.flag_status == 2)
        {
            String photo = emp.group_exercise_id__exercise_routine.muscular_group_id__group_exercise.urlicon;
            if(photo != null && !photo.equals("") && !photo.equals("null"))
            {
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                        .cacheOnDisk(true).resetViewBeforeLoading(true)
                        .showImageOnLoading(R.drawable.proximotreino) // resource or drawable
                        .showImageForEmptyUri(R.drawable.proximotreino) // resource or drawable
                        .showImageOnFail(R.drawable.proximotreino) // resource or drawable
                        .build();
                imageLoader.displayImage(photo, holder.img1, options);
            }
            else
            {
                // create bitmap from resource
                holder.img1.setImageResource(R.drawable.proximotreino);
            }

        }
        else if(emp.flag_status == 3)
        {
            holder.img1.setImageResource(R.drawable.treino_past);
            holder.descricao2.setText(mContext.getString(R.string.exercicios_3));
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }
}
