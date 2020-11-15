package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetomobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import model.Vacinas;

public class AdapterVacinas extends RecyclerView.Adapter<AdapterVacinas.MyViewHolder>{
    Context context;
    ArrayList<Vacinas> vacinas;

    public AdapterVacinas(Context context, ArrayList<Vacinas> vacinas) {
        this.context = context;
        this.vacinas = vacinas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_vacina, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.Container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.nome.setText(vacinas.get(position).getNome());
        holder.descricao.setText(vacinas.get(position).getDescricao());
        holder.indicacao.setText(vacinas.get(position).getIndicacao());
        Picasso.get().load(vacinas.get(position).getImagem()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return vacinas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        RelativeLayout Container;
        TextView nome;
        TextView descricao;
        TextView indicacao;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Container = (RelativeLayout) itemView.findViewById(R.id.container);
            nome = (TextView)itemView.findViewById(R.id.nome);
            descricao = (TextView)itemView.findViewById(R.id.descrica);
            indicacao = (TextView)itemView.findViewById(R.id.indicacao);
            img = (ImageView)itemView.findViewById(R.id.ftt);
        }
    }

}
