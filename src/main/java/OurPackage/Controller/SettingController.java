package OurPackage.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.sound.sampled.FloatControl;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static OurPackage.Controller.DictionaryController.ListLog;
import static OurPackage.Controller.DictionaryController.currentIndex;
import static OurPackage.Controller.MonkeyGameController.DATA2;
import static OurPackage.Module.DatabaseManager.list;
import static OurPackage.Module.SomethingForGame.EmptyFile;
import static OurPackage.Start.*;

public class SettingController extends GeneralController {

    @FXML
    private Pane Back;

    @FXML
    private Pane DisplayContent;

    @FXML
    private Label non;

    @FXML
    private Label timeUsingApp;

    @FXML
    private TextField IPAforWord;

    @FXML
    private TextField MeanOfWord;

    @FXML
    private Pane paneNon;

    @FXML
    private AnchorPane Menu;

    @FXML
    private VBox Menutab;

    @FXML
    private JFXButton but_add;

    @FXML
    private JFXButton but_delete;

    @FXML
    private JFXButton but_diction;

    @FXML
    private JFXButton but_game;

    @FXML
    private JFXButton but_home;

    @FXML
    private JFXButton but_set;

    @FXML
    private JFXButton but_trans;

    @FXML
    private ImageView catcute;

    @FXML
    private ImageView imgBug;

    @FXML
    private ImageView imgSuccess;

    @FXML
    private JFXButton clearBookMark;

    @FXML
    private JFXButton resetActivitiesHistoryApp;

    @FXML
    private Pane inner_pane;

    @FXML
    private JFXListView<String> listWord;

    @FXML
    private Pane menu_inner_pane;

    @FXML
    private CheckBox musicApp;

    @FXML
    private TextField newWord;

    @FXML
    private Pane paneAddNewWordToDictionary;

    @FXML
    private Pane paneDeleteWord;

    @FXML
    private HBox root;

    @FXML
    private TextField searchWordToDelete;

    @FXML
    private JFXButton sendFeedBack;

    @FXML
    private JFXButton doDelete;

    @FXML
    private JFXButton resetApp;

    @FXML
    private JFXButton addNewWordForDic;

    @FXML
    private JFXButton closePaneAddNewWord;

    @FXML
    private TextArea textFeedBack;

    @FXML
    private TextField typeWord;

    @FXML
    private Slider slider_game;



    int countDelete = 0;


    private void actionOfNon() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(4), paneNon);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        timeUsingApp.setText(timeString);


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            updateTimer();
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        DictionaryController.ForSearchingDicWord(searchWordToDelete, list, listWord);

        but_set.setStyle("-fx-background-color: #333333;");

        but_add.setOnAction(e -> {
            if(paneDeleteWord.isVisible() == true) {
                paneDeleteWord.setVisible(false);
                countDelete++;
            }
            paneAddNewWordToDictionary.setVisible(true);
        });

        closePaneAddNewWord.setOnAction(e -> {
            newWord.setText("");
            IPAforWord.setText("");
            typeWord.setText("");
            MeanOfWord.setText("");
            paneAddNewWordToDictionary.setVisible(false);
        });

        but_delete.setOnAction(e -> {
            paneAddNewWordToDictionary.setVisible(false);
            paneDeleteWord.setVisible(countDelete % 2 == 0);
            if(countDelete % 2 != 0) {
                searchWordToDelete.setText("");
                DictionaryController.ForSearchingDicWord(searchWordToDelete, list, listWord);
            }
            countDelete++;
        });

        textFeedBack.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                sendFeedBack.fire();
            }
        });

        doDelete.setOnAction(e -> {
            String wordDelete = listWord.getSelectionModel().getSelectedItem();
            if(wordDelete != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setHeaderText(null);
                alert.initOwner(DisplayContent.getScene().getWindow());
                alert.setContentText("Bạn có chắc chắn muốn xóa từ: " + wordDelete + " không?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("Người dùng đã chọn: Đồng ý");
                        databaseManager.deleteWordFromDB(wordDelete);
                        listWord.getItems().remove(wordDelete);
                        imgBug.setVisible(false);
                        imgSuccess.setVisible(true);
                        non.setText("Thành công xóa từ: " + wordDelete + " !");
                        actionOfNon();
                    } else if (response == ButtonType.CANCEL) {
                        System.out.println("Người dùng đã chọn: Không");
                    }
                });
            } else {
                imgBug.setVisible(true);
                imgSuccess.setVisible(false);
                non.setText("Lỗi : Vui lòng chọn từ muốn xóa !");
                actionOfNon();
            }
        });

        resetActivitiesHistoryApp.setOnAction(e -> {
            if(!ListLog.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setHeaderText(null);
                alert.initOwner(DisplayContent.getScene().getWindow());
                alert.setContentText("Bạn có chắc chắn muốn đặt lại lịch sử hoạt động không?");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        historyActivites.clearHistory();
                        ListLog.clear();
                        imgBug.setVisible(false);
                        currentIndex = 0;
                        imgSuccess.setVisible(true);
                        non.setText("Thành công xóa lịch sử hoạt động !");
                        actionOfNon();
                    } else if (response == ButtonType.CANCEL) {
                        System.out.println("Người dùng đã chọn: Không");
                    }
                });
            } else {
                imgBug.setVisible(true);
                imgSuccess.setVisible(false);
                non.setText("Lịch sử hoạt động trống!");
                actionOfNon();
            }
        });

        addNewWordForDic.setOnAction(e -> {
            String word = newWord.getText();
            String pronounce = IPAforWord.getText();
            String type = typeWord.getText();
            String mean = MeanOfWord.getText();

            if (!word.isEmpty() && !pronounce.isEmpty() && !type.isEmpty() && !mean.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setHeaderText(null);
                alert.setContentText("Bạn có chắc chắn muốn thêm từ: " + word + " không?");
                alert.initOwner(DisplayContent.getScene().getWindow());
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        System.out.println("Người dùng đã chọn: Đồng ý");
                        if(databaseManager.list.containsKey(word)) {
                            non.setText("Từ: " + word + " đã tồn tại !");
                            actionOfNon();
                            imgBug.setVisible(true);
                            imgSuccess.setVisible(false);
                        } else {
                            databaseManager.insertWordToDB(word, pronounce, type, mean);
                            non.setText("Thành công thêm từ: " + word + " !");
                            actionOfNon();
                            imgBug.setVisible(false);
                            imgSuccess.setVisible(true);
                        }
                        newWord.setText("");
                        IPAforWord.setText("");
                        typeWord.setText("");
                        MeanOfWord.setText("");
                        paneAddNewWordToDictionary.setVisible(false);
                    } else if (response == ButtonType.CANCEL) {
                        System.out.println("Người dùng đã chọn: Không");
                    }
                });
            } else {
                imgBug.setVisible(true);
                imgSuccess.setVisible(false);
                non.setText("Lỗi : Vui lòng nhập đủ thông tin từ!");
                actionOfNon();
            }
        });

        clearBookMark.setOnAction(e -> {
            if (!MarkedWord.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Xác nhận");
                alert.setHeaderText(null);
                alert.setContentText("Bạn có chắc chắn muốn đặt lại danh sách từ yêu thích không?");
                alert.initOwner(DisplayContent.getScene().getWindow());
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        bookMark.clearBookmark();
                        imgBug.setVisible(false);
                        imgSuccess.setVisible(true);
                        non.setText("Thành công xóa danh sách từ yêu thích !");
                        actionOfNon();
                    } else if (response == ButtonType.CANCEL) {
                        System.out.println("Người dùng đã chọn: Không");
                    }
                });
            } else {
                imgBug.setVisible(true);
                imgSuccess.setVisible(false);
                non.setText("Danh sách từ yêu thích trống !");
                actionOfNon();
            }
        });

        resetApp.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận");
            alert.setHeaderText(null);
            alert.setContentText("Bạn có chắc chắn muốn đặt lại ứng dụng không?\n\t\t(Mọi thứ đều trở về ban đầu)");
            alert.initOwner(DisplayContent.getScene().getWindow());
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    bookMark.clearBookmark();
                    historyActivites.clearHistory();
                    historySearch.clearHistory();
                    databaseManager.resetData();
                    currentIndex = 0;
                    try {
                        EmptyFile(DATA2);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    imgBug.setVisible(false);
                    imgSuccess.setVisible(true);
                    non.setText("Thành công đặt lại ứng dụng !\n(Khởi động lại sẽ có hiệu lực)");
                    actionOfNon();
                } else if (response == ButtonType.CANCEL) {
                    System.out.println("Người dùng đã chọn: Không");
                }
            });
        });

        sendFeedBack.setOnAction(e -> {
            if (!textFeedBack.getText().isEmpty()) {
                String feedBack = DictionaryController.getTimeNow() + "   -   " + textFeedBack.getText();
                try {
                    FileWriter fw = new FileWriter("./Data/feedback.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(feedBack);
                    bw.newLine();
                    bw.close();
                    fw.close();

                    non.setText("Cảm ơn những đóng góp của bạn !");
                    imgSuccess.setVisible(true);
                    imgBug.setVisible(false);
                    textFeedBack.setText("");
                    actionOfNon();
                } catch (IOException k) {
                    k.printStackTrace();
                }
            } else {
                non.setText("Lỗi : Nội dung đóng góp trống !");
                imgBug.setVisible(true);
                imgSuccess.setVisible(false);
                actionOfNon();
            }
        });

        volumeValue(slider_game, "game");

    }

    // Luu nut keo của am luong game va volume cua no
    public void volumeValue(Slider slider, String Type) {
        prefs = Preferences.userNodeForPackage(SettingController.class);
        double savedValue = prefs.getDouble(Type, 5);
        slider.setValue(savedValue);
        // Cập nhật Preferences mỗi khi giá trị Slider thay đổi
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            prefs.putDouble(Type, newValue.doubleValue());
            slider.setValue(newValue.doubleValue());
            volumeGame = slider_game.getValue();
        });
        volumeGame = slider_game.getValue();
    }

    private void updateTimer() {
        timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timeUsingApp.setText(timeString);
    }

}
