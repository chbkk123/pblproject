package nicarng.selcar.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nicarng.selcar.InnerBoard;
import nicarng.selcar.R;

public class DbboardAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<Dbboard> dbboard;

    public DbboardAdapter(Context context,List<Dbboard> dbboard){
        this.context = context;
        this.dbboard = dbboard;
    }

    @Override
    public int getCount() {
        return dbboard.size();
    }

    @Override
    public Object getItem(int i) {
        return dbboard.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup parent) {
        view = View.inflate(context,R.layout.dbboard,null);
        TextView idx = view.findViewById(R.id.idx);
        idx.setText(dbboard.get(i).getIdx());

        TextView subject = view.findViewById(R.id.head);
        subject.setText(dbboard.get(i).getSubject());

        TextView editor = view.findViewById(R.id.editor);
        editor.setText(dbboard.get(i).getEditor());


        view.setTag(i);

        view.setOnClickListener(this);

        return view;
    }
    public void onClick(View view){

        int i = (Integer) view.getTag();

        Bundle extras = new Bundle();
        extras.putString("idx",dbboard.get(i).getIdx());
        extras.putString("Subject",dbboard.get(i).getSubject());
        extras.putString("Editor",dbboard.get(i).getEditor());

        Intent intent = new Intent(context,InnerBoard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(extras);
        context.startActivity(intent);


    }


}
