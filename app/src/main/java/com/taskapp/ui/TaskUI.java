package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.print.DocFlavor.READER;

import com.taskapp.exception.AppException;
import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.model.User;
import com.taskapp.exception.AppException;

public class TaskUI {
    private final BufferedReader reader;

    private final UserLogic userLogic;

    private final TaskLogic taskLogic;

    private User loginUser;

    public TaskUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        userLogic = new UserLogic();
        taskLogic = new TaskLogic();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
        this.reader = reader;
        this.userLogic = userLogic;
        this.taskLogic = taskLogic;
    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");
        inputLogin();
        // メインメニュー
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                        taskLogic.showAll(loginUser);
                        break;
                    case "2":
                        inputNewInformation();
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    /* ユーザーからメールアドレス、パッケージの入力を受け付け、
        入力を受け付けたものを、
        userLogicのUserオブジェクトが返ってくるloginメソッドに引数として渡し、
     * メールアドレスとパスワードが一致するユーザを取得する
     */
    public void inputLogin() {

        boolean flg = true;

        while (flg) {

            try{

                System.out.print("メールアドレスを入力して下さい：");
                String email = reader.readLine();

                System.out.print("パスワードを入力して下さい：");
                String password = reader.readLine();

                loginUser = userLogic.login(email, password);
                System.out.println();
                flg = false;

            }catch(IOException e){
                e.printStackTrace();
            }catch(AppException e){
                System.out.println(e.getMessage());
            }
            
        }
    }

    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    /* ユーザーから新規登録するタスクの情報を入力してもらい
        入力されたものをcsvに保存する。
     * 
     */
    public void inputNewInformation() {
        boolean flg = true;

        while (flg) {

            try {
                System.out.print("タスクコードを入力してください：");
                String code = reader.readLine();
                if(!isNumeric(code)){
                    System.out.println("コードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }

                System.out.print("タスク名を入力してください：");
                String name = reader.readLine();
                if(!(name.length() <= 10)){
                    System.out.println("タスク名は10文字以内で入力してください");
                    System.out.println();
                    continue;
                }

                System.out.print("担当するユーザーのコードを選択してください：");
                String userCode = reader.readLine();
                if(!isNumeric(code)){
                    System.out.println("コードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }
            
                taskLogic.save(Integer.parseInt(code), name, Integer.parseInt(userCode), loginUser);

                flg = false;
                
            } catch (IOException e) {
                e.printStackTrace();
            }catch(AppException e){
                System.out.println(e.getMessage());
            }
            System.out.println();
        }

    }

    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    public void selectSubMenu() {
        boolean flg = true;

        while (flg) {
            try{

                System.out.println("以下1~2から好きな選択肢を選んでください。");
                System.out.println("1. タスクのステータス変更, 2. メインメニューに戻る");
                System.out.print("選択肢: ");
                String selectSubmenu = reader.readLine();

                switch (selectSubmenu) {
                    case "1":
                        inputChangeInformation();
                        break;
                    case "2":
                        flg = false;
                        break;
                    default:
                        System.out.println("半角で、選択肢にある整数を入力してください。");
                        break;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            
        }
    }

    /**
     * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
     */
    public void inputChangeInformation() {

        boolean flg = true;
        while (flg) {

            try{
                System.out.print("ステータスを変更するタスクコードを入力してください：");
                String code = reader.readLine();
                if(!isNumeric(code)){
                    System.out.println("コードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }
                
                System.out.println("どのステータスに変更するか選択してください。");
                System.out.print("1. 着手, 2. 完了");
                String status = reader.readLine();
                if(!isNumeric(status)){
                    System.out.println("コードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }

                if(status != "1" || status == "2"){
                    
                }
                

            }catch(IOException e){
                e.printStackTrace();
            }
            
        }
    }

    /**
     * ユーザーからのタスク削除情報を受け取り、タスクを削除します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#delete(int)
     */
    // public void inputDeleteInformation() {
    // }

    /**
     * 指定された文字列が数値であるかどうかを判定します。
     * 負の数は判定対象外とする。
     *
     * @param inputText 判定する文字列
     * @return 数値であればtrue、そうでなければfalse
     */
    public boolean isNumeric(String inputText) {
        return inputText.chars().allMatch(c -> Character.isDigit(c));
    }
}