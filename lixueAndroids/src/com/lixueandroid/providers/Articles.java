/**
 * 
 */
/**
 *
 */
package com.lixueandroid.providers;

import android.net.Uri;

/**
 *	文章类
 */
public class Articles {
        /*Data Field*/
        public static final String ID = "_id";
        public static final String TITLE = "_title";
        public static final String ABSTRACT = "_abstract";
        public static final String URL = "_url";

        /*Default sort order*/
        public static final String DEFAULT_SORT_ORDER = "_id asc";

        /*Call Method*/
        public static final String METHOD_GET_ITEM_COUNT = "METHOD_GET_ITEM_COUNT";
        public static final String KEY_ITEM_COUNT = "KEY_ITEM_COUNT";

        /*Authority*/
        public static final String AUTHORITY = "com.lixueandroid.providers.articles";

        /*Match Code*/
        public static final int ITEM = 1;
        public static final int ITEM_ID = 2;
        public static final int ITEM_POS = 3;

        /*MIME*/
        public static final String CONTENT_TYPE = "vnd.com.lixueandroid.cursor.dir/vnd.com.lixueandroid.article";
        public static final String CONTENT_ITEM_TYPE = "vnd.com.lixueandroid.cursor.item/vnd.com.lixueandroid.article";
        /*
         * URI的全称是Universal Resource Identifier，即通用资源标志符，通过它用来唯一标志某个资源在网络中的位置，
         * 它的结构和我们常见的HTTP形式URL是一样的，其实我们可以把常见的HTTP形式的URL看成是URI结构的一个实例，
         * URI是在更高一个层次上的抽象。在Android系统中，它也定义了自己的用来定义某个特定的Content Provider的URI结构，它通常由四个组件来组成
         *如： [content://][shy.luo.providers.articles][/item][/123]
         *A组件称为Scheme，它固定为content://
         *B组件称为Authority，它唯一地标识了一个特定的Content Provider
         *C组件称为资源路径，它表示所请求的资源的类型
         *D组件称为资源ID，它表示所请求的是一个特定的资源，它通常是一个数字，对应前面我们所介绍的数据库表中的_id字段的内容，它唯一地标志了某一种资源下的一个特定的实例。
         * */
        /*Content URI*/
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");
        public static final Uri CONTENT_POS_URI = Uri.parse("content://" + AUTHORITY + "/pos");
}