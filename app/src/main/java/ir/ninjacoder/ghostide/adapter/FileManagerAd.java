package ir.ninjacoder.ghostide.adapter;

import ir.ninjacoder.ghostide.R;
import ir.ninjacoder.ghostide.config.AmazonClassHelper;
import ir.ninjacoder.ghostide.databin.FileMaker;
import ir.ninjacoder.ghostide.folder.FileHelper;
import ir.ninjacoder.ghostide.folder.FileIconHelper;
import ir.ninjacoder.ghostide.glidecompat.GlideCompat;
import ir.ninjacoder.ghostide.interfaces.FileCallBack;
import ir.ninjacoder.ghostide.marco.FileCounter;
import ir.ninjacoder.ghostide.marco.binder.BinderRecyclerview1;
import ir.ninjacoder.ghostide.marco.editorface.ClassNodePaserImpl;
import ir.ninjacoder.ghostide.utils.AnimUtils;
import ir.ninjacoder.ghostide.utils.ObjectUtils;
import ir.ninjacoder.ghostide.utils.FileUtil;
import ir.ninjacoder.ghostide.utils.MFileClass;
import ir.ninjacoder.ghostide.widget.component.fastscrollcompat.PopupTextProvider;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

// fileListItem
public class FileManagerAd extends RecyclerView.Adapter<FileManagerAd.VH>
    implements PopupTextProvider {
  public static boolean Files = false;
  public static boolean Folder = false;
  protected Context context;
  protected onClick click;
  protected ViewType viewType = ViewType.ROW;
  protected List<HashMap<String, Object>> filteredFiles;
  protected HashMap<String, Object> name = new HashMap<>();
  private List<HashMap<String, Object>> files = new ArrayList<>();
  protected GridLayoutManager g;

  public FileManagerAd(List<HashMap<String, Object>> files, Context context, onClick click) {
    this.context = context;
    this.files = files;
    this.filteredFiles = files;
    this.click = click;
  }

  @Override
  public int getItemCount() {
    return filteredFiles.size();
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parnt, int viewt) {
    View view =
        LayoutInflater.from(parnt.getContext())
            .inflate(
                viewt == ViewType.ROW.getValue()
                    ? R.layout.folder_remster
                    : R.layout.folder_layout_grid,
                parnt,
                false);
    return new VH(view);
  }

  @Override
  public void onBindViewHolder(VH viewHolder, int pos) {
    View view = viewHolder.itemView;
    AnimUtils.Sacla(viewHolder.itemView);
    setSettingTextView(viewHolder.folderName);
    var myfile = new File(filteredFiles.get(pos).get("path").toString());
    viewHolder.folderName.setText(myfile.getName());
    FileIconHelper fileIconHelper = new FileIconHelper(myfile.toString());
    viewHolder.icon.setImageResource(fileIconHelper.getFileIcon());
    fileIconHelper.setDynamicFolderEnabled(true);

    fileIconHelper.setEnvironmentEnabled(true);
    if (FileUtil.isDirectory(filteredFiles.get(pos).get("path").toString())) {
      Folder = true;
      Files = false;
      fileIconHelper.bindIcon(viewHolder.icon);
      FileCounter mfileC = new FileCounter(viewHolder.tvTools);
      mfileC.execute(myfile.toString());
      if (viewType == ViewType.ROW) {
        viewHolder.icon.setPadding(8, 8, 8, 8);
        ObjectUtils.shp(viewHolder.icon);
      }

      viewHolder.tvTools.setText("");
    } else if (FileUtil.isExistFile(filteredFiles.get(pos).get("path").toString())) {
      viewHolder.icon.setPadding(0, 0, 0, 0);
      getTime(myfile.toString(), viewHolder.tvTools);
      viewHolder.icon.setBackgroundColor(0);
      if (BinderRecyclerview1.TaskVideo(myfile.toString())) {
        GlideCompat.GlideNormal(viewHolder.icon, myfile.toString());
      } else if (BinderRecyclerview1.PhotoView(myfile.toString())) {
        GlideCompat.GlideNormal(viewHolder.icon, myfile.toString());
      }
      if (myfile.toString().endsWith(".xml")) {
        GlideCompat.LoadVector(myfile.toString(), viewHolder.icon);

      } else if (myfile.toString().endsWith(".mp3")) {
        GlideCompat.GlideLoadMp3(viewHolder.icon, myfile.toString());
      } else if (myfile.toString().endsWith(".svg")) {
        GlideCompat.LoadSvg(myfile.toString(), viewHolder.icon);
      } else if (myfile.toString().endsWith(".pdf")) {
        try {
          GlideCompat.loadImgPdf(myfile, viewHolder.icon);
        } catch (IOException err) {
          viewHolder.icon.setImageResource(R.drawable.ic_material_pdf);
        }
      } else if (myfile.toString().endsWith(".apk")) {
        GlideCompat.LoadApkFile(myfile.toString(), viewHolder.icon);
      } else if (myfile.toString().endsWith(".vsix")) {
        GlideCompat.LoadIconVsCode(myfile.toString(), viewHolder.icon);

      } else if (myfile.toString().endsWith(".g4")) {
        AmazonClassHelper.getScanAntlr4Grammer(viewHolder.icon, myfile.toString());
      } else if (myfile.toString().endsWith(".AA")) {
        GlideCompat.LoadIconTheme(myfile.toString(), viewHolder.icon);
      }
    }

    viewHolder.itemView.setClickable(true);
  }

  @NonNull
  public HashMap<String, Object> getItem(int position) {
    return filteredFiles.get(position);
  }

  @Override
  public long getItemId(int position) {
    HashMap<String, Object> mmap = getItem(position);
    return Objects.hash(mmap);
  }

  @Override
  @NonNull
  public CharSequence getPopupText(int position) {
    HashMap<String, Object> map = getItem(position);
    return map.get("path").toString().substring(0, 1).toUpperCase();
  }

  public void setSettingTextView(TextView tv) {
    if (tv != null) {
      tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
      tv.setMarqueeRepeatLimit(-1);
      tv.setSelected(true);
      tv.setSingleLine(true);
    }
  }

  public void addItem(int Position) {
    notifyItemInserted(Position);
  }

  public void removedItem(int pos) {
    files.remove(pos);
    notifyItemRemoved(pos);
  }

  public void makeFile(String path) {
    var filemaker = new FileMaker(context);
    filemaker.setFolderName(path);
    filemaker.setCallBack(
        new FileCallBack() {

          @Override
          public void onError(String error) {
            Toast.makeText(context, error, 2).show();
          }

          @Override
          public void onDoneMakeFile(String toast) {
            // notifyDataSetChanged();
          }
        });
  }

  public void search(String query) {
    if (query.length() > 0) {
      List<HashMap<String, Object>> result = new ArrayList<>();
      for (HashMap<String, Object> file : this.files) {
        if (file.get("path").toString().toLowerCase().contains(query.toLowerCase())) {
          result.add(file);
        }
      }

      this.filteredFiles = result;
      notifyDataSetChanged();
    } else {
      this.filteredFiles = this.files;
      notifyDataSetChanged();
    }
  }

  private void getTime(String path, TextView view) {
    try {

      if (view != null) {
        view.setText(
            MFileClass.convertBytes(FileUtil.getFileLength(path))
                .concat(", ".concat(MFileClass.getLastModifiedOfFile(path, "HH:mm,dd/MM/yyyy"))));
      }
    } catch (Exception err) {
      err.printStackTrace();
    }
  }

  public String getMp3Format(String txt) {
    List<String> list = Arrays.asList(FileHelper.AUDIO_FILES);
    for (var item : list) {
      return item + txt;
    }
    return txt;
  }

  public interface onClick {
    public void onClick(View view, int pos);

    public void onLongClick(View view, int pos);
  }

  public class VH extends RecyclerView.ViewHolder {
    protected TextView folderName, tvTools;
    protected LinearLayout roots;
    protected ImageView icon;
    View getPos;

    public VH(View view) {
      super(view);
      getPos = view;
      folderName = view.findViewById(R.id.folderName);
      tvTools = view.findViewById(R.id.tvTools);
      roots = view.findViewById(R.id.roots);
      icon = view.findViewById(R.id.icon);
      roots.setOnClickListener(
          c -> {
            click.onClick(c, getPosition());
          });
      roots.setOnLongClickListener(
          v -> {
            click.onLongClick(v, getPosition());
            return false;
          });
    }
  }

  @Override
  public int getItemViewType(int pos) {
    return viewType.getValue();
  }

  public void setViewType(ViewType viewType) {
    this.viewType = viewType;
    notifyDataSetChanged();
  }
}