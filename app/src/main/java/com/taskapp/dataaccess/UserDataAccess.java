package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.taskapp.model.User;
public class UserDataAccess {
    private final String filePath;

    public UserDataAccess() {
        filePath = "app/src/main/resources/users.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     */
    public UserDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * メールアドレスとパスワードを基にユーザーデータを探します。
     * @param email メールアドレス
     * @param password パスワード
     * @return 見つかったユーザー
     */
    /* user.csvから1行ずつ読み込み、","で区切って配列に代入する。
     * ただ、受け取ったメールアドレスとパスワードが一致しないものは飛ばし、
     * 一致するuser情報を基にuserインスタンスを生成し、生成したインスタンスを呼び出し元に返す。
     */
    public User findByEmailAndPassword(String email, String password) {
        User user = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if(!(values[2].equals(email) && values[3].equals(password))) continue;

                int code = Integer.parseInt(values[0]);
                String name = values[1];
                String userEmail = values[2];
                String userPassword = values[3];

                user = new User(code, name, userEmail, userPassword);
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }
        return user;
    }

    /**
     * コードを基にユーザーデータを取得します。
     * @param code 取得するユーザーのコード
     * @return 見つかったユーザー
     */
    /* 受け取ったコードを基に該当するユーザー情報をcsvから取得する。
     * 取得した、ユーザー情報を持った、インスタンスを返す。
     */
    public User findByCode(int code) {
        User user = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                int useCode = Integer.parseInt(values[0]);

                if(useCode != code) continue;
                    
                String name = values[1];
                String email = values[2];
                String password = values[3];
                
                user = new User(useCode, name, email, password);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}
