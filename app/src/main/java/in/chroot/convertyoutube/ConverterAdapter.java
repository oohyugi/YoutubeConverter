package in.chroot.convertyoutube;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

import in.chroot.convertyoutube.api.VideoMdl;

/**
 * Created by yogi on 07/11/16.
 */
public class ConverterAdapter extends
        RecyclerView.Adapter<ConverterAdapter.ViewHolder> {

    private static final String TAG = ConverterAdapter.class.getSimpleName();

    private Context context;
    private List<VideoMdl.DATA> list;
    private OnItemClickListener onItemClickListener;

    public ConverterAdapter(Context context, List<VideoMdl.DATA> list,
                            OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Todo deklerasiin widget disini
        TextView tvTitle,tvFormat,tvQuality;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFormat = (TextView)itemView.findViewById(R.id.itemFormat);
            tvQuality = (TextView)itemView.findViewById(R.id.itemQuality);


        }

        public void bind(final VideoMdl.DATA model,
                         final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getLayoutPosition(),model);

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_converter, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoMdl.DATA item = list.get(position);

        //Todo: Setup viewholder for item
        holder.tvFormat.setText(item.format);
        holder.tvQuality.setText(item.quality);
        holder.bind(item, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, VideoMdl.DATA model);
    }

}