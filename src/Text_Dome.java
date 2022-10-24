import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.lang3.StringUtils;

public class Text_Dome implements ActionListener {
    public static Frame frame;

    public static TextArea text;

    private Menu menu_File;

    private Menu menu_Other;

    public MenuItem menuItem_Save;

    public MenuItem menuItem_Load;

    public MenuItem menuItem_Quit;

    public MenuItem menuItem_Help;

    public MenuItem menuItem_About;

    private String codeContent;

    private JPanel contentPanel_Text;

    private JLabel contentPanel_QRImg;

    public static String QRCodeName;

    public JPanel contentPanelAll;

    public JButton button_RedaCil;

    public JButton button_Sting;

    public JButton button_QRCode;

    private JPanel contentPanel_Button;

    private JPanel contentPanel_TxtAndButton;

    public String ret;

    String path = "QRImgs";

    static String home2;

    private static final int CODE_WIDTH = 300;

    private static final int CODE_HEIGHT = 300;

    private static final int FRONT_COLOR = 0;

    private static final int BACKGROUND_COLOR = 16777215;

    public Text_Dome() {
        this.frame = new Frame("字符串转二维码");
        this.frame.setBounds(200, 200, 800, 400);
        MenuBar menuBar = new MenuBar();
        this.frame.setMenuBar(menuBar);
        this.menu_File = new Menu("文件");
        this.menu_Other = new Menu("其它");
        menuBar.add(this.menu_File);
        menuBar.add(this.menu_Other);
        this.menuItem_Save = new MenuItem("保留");
        this.menuItem_Load = new MenuItem("读取");
        this.menuItem_Quit = new MenuItem("退出");
        this.menu_File.add(this.menuItem_Save);
        this.menu_File.add(this.menuItem_Load);
        this.menu_File.addSeparator();
        this.menu_File.add(this.menuItem_Quit);
        this.button_RedaCil = new JButton("读取粘贴版内容");
        this.button_Sting = new JButton("字符串转二维码");
        this.button_QRCode = new JButton("生成微步链接");
        this.button_RedaCil.setContentAreaFilled(false);
        this.button_Sting.setContentAreaFilled(false);
        this.button_QRCode.setContentAreaFilled(false);
        this.contentPanelAll = new JPanel();
        this.frame.add(this.contentPanelAll);
        this.contentPanelAll.setLayout(new GridLayout(1, 2, 2, 2));
        this.contentPanel_TxtAndButton = new JPanel();
        this.contentPanel_TxtAndButton.setLayout(new BorderLayout());
        this.contentPanelAll.add(this.contentPanel_TxtAndButton);
        this.contentPanel_Text = new JPanel();
        this.contentPanel_TxtAndButton.add(this.contentPanel_Text, "South");
        this.contentPanel_Text.setLayout(new GridLayout());
        this.contentPanel_Button = new JPanel();
        this.contentPanel_Button.setLayout(new GridLayout(1, 3));
        this.contentPanel_TxtAndButton.add(this.contentPanel_Button);
        this.contentPanel_Button.add(this.button_RedaCil, "North");
        this.contentPanel_Button.add(this.button_Sting, "North");
        this.contentPanel_Button.add(this.button_QRCode, "North");
        this.contentPanel_QRImg = new JLabel();
        this.contentPanelAll.add(this.contentPanel_QRImg);
        this.menuItem_Help = new MenuItem("");
        this.menu_Other.add(this.menuItem_Help);
        this.menuItem_About = new MenuItem("");
        this.menu_Other.add(this.menuItem_About);
        this.text = new TextArea("", 20, 20);
        this.contentPanel_Text.add("Center", this.text);
        this.frame.setVisible(true);
        this.frame.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {}

            public void windowClosing(WindowEvent e) {
                String home = String.valueOf(FileSystemView.getFileSystemView().getHomeDirectory());
                Text_Dome.this.home2 = home + "\\QRImgs";
                File file = new File(Text_Dome.this.home2);
                if (!file.exists())
                    System.exit(0);
                try {
                    Text_Dome.this.delImag();
                    System.exit(0);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            public void windowClosed(WindowEvent e) {}

            public void windowIconified(WindowEvent e) {}

            public void windowDeiconified(WindowEvent e) {}

            public void windowActivated(WindowEvent e) {}

            public void windowDeactivated(WindowEvent e) {}
        });
        this.menuItem_Save.addActionListener(this);
        this.menuItem_Load.addActionListener(this);
        this.menuItem_Quit.addActionListener(this);
        this.menuItem_Help.addActionListener(this);
        this.menuItem_About.addActionListener(this);
        this.button_RedaCil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable clipTf = sysClip.getContents(null);
                if (clipTf != null)
                    if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor))
                        try {
                            Text_Dome.this.ret = (String)clipTf.getTransferData(DataFlavor.stringFlavor);
                            Text_Dome.this.text.setText(Text_Dome.this.ret);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
            }
        });
        this.button_Sting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Text_Dome.this.codeContent = String.valueOf(Text_Dome.this.text);
                Text_Dome.this.codeContent = StringUtils.substringAfter(Text_Dome.this.codeContent, "=");
                Text_Dome.this.codeContent = StringUtils.substringBefore(Text_Dome.this.codeContent, ",editable,s");
                Text_Dome.createCodeToFile(Text_Dome.this.codeContent, new File(Text_Dome.this.path), null);
                Text_Dome.this.contentPanel_QRImg.setIcon(new ImageIcon(Text_Dome.QRCodeName));
                Text_Dome.this.contentPanel_QRImg.setHorizontalAlignment(0);
                Text_Dome.this.contentPanel_QRImg.setVisible(true);
            }
        });
        this.button_QRCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Text_Dome.this.codeContent = String.valueOf(Text_Dome.this.text);
                Text_Dome.this.codeContent = StringUtils.substringAfter(Text_Dome.this.codeContent, "=");
                Text_Dome.this.codeContent = StringUtils.substringBefore(Text_Dome.this.codeContent, ",editable,s");
                String UrlCode = "https://x.threatbook.com/v5/ip/" + Text_Dome.this.codeContent;
                if (Text_Dome.this.codeContent == null || "".equals(Text_Dome.this.codeContent)) {
                    JOptionPane.showMessageDialog(null, "请输入查询的内容", "错误", 0);
                    System.out.println("二维码内容为空，不能进行操作");
                    return;
                }
                Text_Dome.createCodeToFile(UrlCode, new File(Text_Dome.this.path), null);
                Text_Dome.this.contentPanel_QRImg.setIcon(new ImageIcon(Text_Dome.QRCodeName));
                Text_Dome.this.contentPanel_QRImg.setHorizontalAlignment(0);
                Text_Dome.this.contentPanel_QRImg.setVisible(true);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.menuItem_Save) {
            SaveFile();
        } else if (source == this.menuItem_Load) {
            loadFile();
        } else if (source == this.menuItem_Quit) {
            String home = String.valueOf(FileSystemView.getFileSystemView().getHomeDirectory());
            this.home2 = home + "\\QRImgs";
            File file = new File(this.home2);
            if (!file.exists())
                System.exit(0);
            try {
                delImag();
                System.exit(0);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (source == this.menuItem_About) {
            JOptionPane.showMessageDialog(null, "作者：0Fly");
        } else if (source == this.menuItem_Help) {
            JOptionPane.showMessageDialog(null, "一个将字符串转成二维码的小工具！", "帮助", -1);
        }
    }

    static void delImag2(String imagePathName) {
        try {
            String imagPath = imagePathName;
            imagPath = imagPath.toString();
            File delImag = new File(imagPath);
            delImag.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void delImag() throws IOException {
        Path path1 = Paths.get(this.home2, new String[0]);
        Files.walkFileTree(path1, new SimpleFileVisitor<Path>() {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Text_Dome.this.delImag2(String.valueOf(file));
                System.out.printf("文件被删除: %s%n", new Object[] { file });
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Text_Dome.this.delImag2(String.valueOf(dir));
                System.out.printf("文件夹被删除:%s%n", new Object[] { dir });
                return FileVisitResult.CONTINUE;
            }
        });
    }

    void SaveFile() {
        FileDialog fileDialog = new FileDialog(this.frame, "请输入要保存的文件名称", 1);
                fileDialog.setVisible(true);
        if (fileDialog == null || fileDialog.getFile() == null || "".equals(fileDialog.getFile()))
            return;
        String fileName = fileDialog.getFile();
        String filePath = fileDialog.getDirectory() + fileName + ".txt";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(this.text.getText());
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void loadFile() {
        FileDialog fileDialog = new FileDialog(this.frame, "请选择要读取的文件", 0);
                fileDialog.setVisible(true);
        if (fileDialog == null || fileDialog.getFile() == null || "".equals(fileDialog.getFile()))
            return;
        this.text.setText("");
        String fileName = fileDialog.getFile();
        String filePath = fileDialog.getDirectory() + fileName;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
                this.text.setText(this.text.getText() + line + System.getProperty("line.separator"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createCodeToFile(String codeContent, File codeImgFileSaveDir, String fileName) {
        try {
            if (codeContent == null || "".equals(codeContent)) {
                JOptionPane.showMessageDialog(null, "不能将字符串转成二维码！", "错误", 0);
                System.out.println("");
                return;
            }
            codeContent = codeContent.trim();
            if (codeImgFileSaveDir == null || codeImgFileSaveDir.isFile()) {
                codeImgFileSaveDir = FileSystemView.getFileSystemView().getHomeDirectory();
                System.out.printf("二维码存放的目录为空，默认放在桌面", new Object[0]);
            }
            if (!codeImgFileSaveDir.exists()) {
                codeImgFileSaveDir.mkdir();
                System.out.printf("二维码图片存在的目录不存在，开始创建", new Object[0]);
            }
            if (fileName == null || "".equals(fileName)) {
                fileName = (new Date()).getTime() + ".png";
                System.out.printf("二维码图片文件名为空，随机生成PNG格式图片", new Object[0]);
            }
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, Integer.valueOf(1));
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(codeContent, BarcodeFormat.QR_CODE, 300, 300, hints);
            BufferedImage bufferedImage = new BufferedImage(300, 300, 4);
            for (int x = 0; x < 300; x++) {
                for (int y = 0; y < 300; y++)
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0 : 16777215);
            }
            File codeImFile = new File(codeImgFileSaveDir, fileName);
            ImageIO.write(bufferedImage, "png", codeImFile);
            QRCodeName = codeImFile.getPath();
            System.out.printf("二维码图片生成成功"+ codeImFile.getPath(), new Object[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
