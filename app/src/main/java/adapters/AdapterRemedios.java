package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetomobile.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.Remedios;

public class AdapterRemedios extends RecyclerView.Adapter<AdapterRemedios.MyViewHolder>{

    Context context;
    ArrayList<Remedios> remedios;


    public AdapterRemedios(Context context, ArrayList<Remedios> remedios) {
        this.context = context;
        this.remedios = remedios;
    }

    public AdapterRemedios(FirebaseRecyclerOptions<Remedios> options) {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_remedios, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nome.setText(remedios.get(position).getNome());
        holder.descricao.setText(remedios.get(position).getDescricao());
        holder.quantidade.setText(remedios.get(position).getQuantidade());
        Picasso.get().load(remedios.get(position).getImagem()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return remedios.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView nome;
        TextView descricao;
        TextView quantidade;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = (TextView)itemView.findViewById(R.id.n);
            descricao = (TextView)itemView.findViewById(R.id.disc);
            quantidade = (TextView)itemView.findViewById(R.id.q);
            img = (ImageView)itemView.findViewById(R.id.ftt);
        }
    }


}
