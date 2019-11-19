package pmd.di.ubi.pt.chatamigavel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView texto;
    private ConstraintLayout main;

    public MessageViewHolder(View v) {
        super(v);
        texto = (TextView) itemView.findViewById(R.id.text_message);
    }

    void setDetails(final Message message, int id, final Context context, final String concIds) {

        ConstraintLayout.LayoutParams lp2 = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(message.getIdSender()==id){
            texto.setLayoutParams(lp2);
            texto.setBackgroundResource(R.drawable.mensagem_out);
        }
        else {
            main = (ConstraintLayout) itemView.findViewById(R.id.linear);
            main.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Informaçoes ")
                            .setMessage("Deseja eliminar?")
                            .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseDatabase.getInstance().getReference("messages").child(concIds).child(message.getId()).removeValue();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.show();
                    return false;
                }
            });
        }

        texto.setText(message.getMessageText());
}

}