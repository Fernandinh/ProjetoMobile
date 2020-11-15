package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetomobile.R;

import java.util.List;

import model.ConsultasAntigas;

public class AdapterConsultasAntigas extends RecyclerView.Adapter<AdapterConsultasAntigas.MyViewHolder> {

    Context context;
    List<ConsultasAntigas> consultasAntigas;

    public AdapterConsultasAntigas(Context context, List<ConsultasAntigas> consultasAntigas) {
        this.context = context;
        this.consultasAntigas = consultasAntigas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_consultas_antigas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.Container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.diaa.setText(consultasAntigas.get(position).getData());
        holder.descricaoo.setText(consultasAntigas.get(position).getDescricao());
        holder.horarioo.setText(consultasAntigas.get(position).getHorario());
        holder.locall.setText(consultasAntigas.get(position).getLocal());
        holder.doutorr.setText(consultasAntigas.get(position).getDoutor());

    }

    @Override
    public int getItemCount() {
        return consultasAntigas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout Container;
        TextView diaa;
        TextView descricaoo;
        TextView horarioo;
        TextView locall;
        TextView doutorr;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Container = (RelativeLayout) itemView.findViewById(R.id.container);
            diaa = (TextView) itemView.findViewById(R.id.dia);
            descricaoo = (TextView) itemView.findViewById(R.id.descricao);
            horarioo = (TextView)itemView.findViewById(R.id.horario) ;
            locall = (TextView) itemView.findViewById(R.id.local);
            doutorr = (TextView) itemView.findViewById(R.id.doutor);



        }
    }

}
