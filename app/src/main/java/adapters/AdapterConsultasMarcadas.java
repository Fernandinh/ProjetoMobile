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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import model.ConsultasMarcadas;

public class AdapterConsultasMarcadas extends RecyclerView.Adapter<AdapterConsultasMarcadas.MyViewHolder> {

    Context context;
    List<ConsultasMarcadas> consultasMarcadas;


    public AdapterConsultasMarcadas(Context context, List<ConsultasMarcadas> consultasMarcadas) {
        this.context = context;
        this.consultasMarcadas = consultasMarcadas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_consultas_marcadas, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.nomee.setText(consultasMarcadas.get(position).getNome());
        holder.diaa.setText(consultasMarcadas.get(position).getData());
        holder.especialidadee.setText(consultasMarcadas.get(position).getEspecialidade());
        holder.horarioo.setText(consultasMarcadas.get(position).getHora());
        holder.locall.setText(consultasMarcadas.get(position).getLocal());
        holder.doutorr.setText(consultasMarcadas.get(position).getMedico());
        Picasso.get().load(consultasMarcadas.get(position).getFoto()).into(holder.ft);
    }

    @Override
    public int getItemCount() {
        return consultasMarcadas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomee;
        TextView diaa;
        TextView especialidadee;
        TextView horarioo;
        TextView locall;
        TextView doutorr;
        ImageView ft;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomee = (TextView) itemView.findViewById(R.id.nomeeE);
            diaa = (TextView) itemView.findViewById(R.id.dia);
            especialidadee = (TextView) itemView.findViewById(R.id.descricao);
            horarioo = (TextView)itemView.findViewById(R.id.horario) ;
            locall = (TextView) itemView.findViewById(R.id.local);
            doutorr = (TextView) itemView.findViewById(R.id.doutor);
            ft = itemView.findViewById(R.id.ft);
        }
    }
}
