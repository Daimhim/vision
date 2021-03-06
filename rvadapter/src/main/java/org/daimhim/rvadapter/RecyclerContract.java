package org.daimhim.rvadapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * 项目名称：org.daimhim.rvadapter
 * 项目版本：RVDecoration
 * 创建人：Daimhim
 * 创建时间：2017/12/25 15:20
 * 修改人：Daimhim
 * 修改时间：2017/12/25 15:20
 * 类描述：
 * 修改备注：
 * @author Daimhim
 */

public interface RecyclerContract {
    /**
     * 提供给外部的 接口  传入此接口 监听点击事件
     */
    interface OnItemClickListener {
        /**
         * 点击事件
         * @param view View
         * @param position position
         */
        void onItemClick(View view, int position);
    }

    /**
     *  提供给外部的 接口  传入此接口 监听点击事件
     */
    interface OnItemLongClickListener {
        /**
         * 长点击事件
         * @param view  view
         * @param position position
         */
        void onItemLongClick(View view, int position);
    }

    /**
     * 空界面
     */
    interface EmptyContract {
        /**
         * 是否空页面
         * @return true Y false N
         */
        boolean isEmptyView();

        /**
         * 设置空页面
         * @param holder  一个Item单元
         * @param position  位置
         */
        void onBindEmptyViewHolder(RecyclerViewClick.ClickViewHolder holder, int position);

        /**
         * 设置空页面
         * @param parent 父布局
         * @param viewType 类型
         * @return 视图
         */
        RecyclerViewClick.ClickViewHolder onCreateEmptyViewHolder(ViewGroup parent, int viewType);
    }

    /**
     * Group 点击事件
     */
    interface OnGroupItemClickListener {
        /**
         * 点击事件
         * @param view 视图
         * @param groupPosition group位置
         */
        void onGroupItemClick(View view, int groupPosition);
    }

    /**
     * Group Loong OnClick
     */
    interface OnGroupItemLongClickListener {
        /**
         *  Group Loong OnClick
         * @param view View
         * @param groupPosition Group Positiong
         */
        void onGroupItemLongClick(View view, int groupPosition);
    }

    /**
     * Child OnClick
     */
    interface OnChildItemClickListener {
        /**
         * Child OnClick fun
         * @param view View
         * @param groupPosition Group Position
         * @param childPosition Child Position
         */
        void onChildItemClick(View view, int groupPosition, int childPosition);
    }

    /**
     * Child Long OnClick
     */
    interface OnChildItemLongClickListener {
        /**
         *  Child Long Onclick
         * @param view View
         * @param groupPosition group position
         * @param childPosition child position
         */
        void onChildItemLongClick(View view, int groupPosition, int childPosition);
    }

    /**
     * Adapter规范
     * @param <Ts> List
     * @param <T> Item
     */
    interface SpecificationContract<Ts,T> {
        /**
         * 刷新
         * @param ts 数据类型
         */
        void onRefresh(Ts ts);

        /**
         * 添加多条
         * @param ts 数据类型
         * @param position 位置
         */
        void onLoad(Ts ts, int position);

        /**
         * 插入
         * @param t  数据类型
         * @param position 位置
         *                 该方法可以和add合并
         */
        void insertItem(T t, int position);

        /**
         * 删除
         * @param position 位置
         */
        void deleteItem(int position);

        /**
         * 替换
         * @param t 数据类型
         * @param position 位置
         */
        void replaceItem(T t, int position);

        /**
         * 获取数据
         * @param position  位置
         * @return 数据类型
         */
        T getItem(int position);
    }

    /**
     *  Adapter规范
     * @param <G> Group Type
     * @param <C> Child Type
     * @param <T> Child Item Type
     */
    interface ExpandableContract<G,C,T> {
        /**
         * 刷新
         * @param ts ts 数据类型
         */
        void onRefresh(G ts);

        /**
         * 添加多条
         * @param ts ts 数据类型
         * @param groupPosition Group Position
         * @param position Group position
         */
        void onLoad(G ts, int groupPosition, int position);

        /**
         * 获取数据
         * @param groupPosition group Position
         * @param childPosition child Position
         * @return Child Data
         */
        T getChildItem(int groupPosition, int childPosition);

        /**
         * 获取数据
         * @param groupPosition group Position
         * @return Child List Data
         */
        C getChild(int groupPosition);
    }

    /**
     * Adapter规范
     * @param <Ts> List Type
     * @param <T> Item Type
     */
    interface SimpleContract<Ts,T> {
        /**
         * 刷新
         * @param ts 数据类型
         */
        void onRefresh(Ts ts);

        /**
         * 获取数据
         * @param position  位置
         * @return 数据类型
         */
        T getItem(int position);

        /**
         * 加载
         * @param ts 数据
         */
        void onLoad(Ts ts);
    }
}
