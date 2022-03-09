package nardi.francisco.juan.passazo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class clientes_adapter extends ArrayAdapter<ConsultaItem> {





    public clientes_adapter(Context context, List<ConsultaItem> objects) {

        super(context, 0, objects);

    }

    public clientes_adapter(Context context) {//, Integer id_Evento) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Obteniendo una instancia del inflater
        // LayoutInflater inflater = (LayoutInflater) getContext()
        //       .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;
        if (listItemView == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            listItemView = vi.inflate(R.layout.item_cliente, null);
        }
        // View listItemView = convertView;

        //Comprobando si el View no existe
       /* if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            listItemView = inflater.inflate(
                    R.layout.participante_evento_row,     //mmmm verrr
                    parent,
                    false);
        }*/


        TextView nombre = (TextView) listItemView.findViewById(R.id.item_cliente_nombre);
        TextView codigo = (TextView) listItemView.findViewById(R.id.item_cliente_codigo);
        //TextView cod_emp = (TextView) listItemView.findViewById(R.id.cod_emp_ic);
        TextView cuit = (TextView) listItemView.findViewById(R.id.item_cliente_cuit);


        //   TextView tel = (TextView)listItemView.findViewById(R.id.mail_telef);
        //ImageView categoria = (ImageView)listItemView.findViewById(R.id.category);


        //Obteniendo instancia de la Tarea en la posición actual
        ConsultaItem item = getItem(position);

        nombre.setText(item.getNombre());
        codigo.setText(item.getObservaciones());
        //  cod_emp.setText(item.getCod_emp());
        cuit.setText(item.getFecha());

        return listItemView;


    }
}
