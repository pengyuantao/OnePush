package com.peng.one.push.core;

import android.content.Context;

/**
 * Define a unified interface for the following implementation
 * Created by pyt on 2017/5/9.
 */

public interface IPushClient {

    /**
     * The Context object initialization time passed, the user message push registered to use
     * @param context
     */
    void initContext(Context context);

    /**
     * Registered push
     *
     */
    void register();


    /**
     * Cancel the registration of push
     *
     */
    void unRegister();


    /**
     * Binding alias
     *
     * @param alias alias
     */
    void bindAlias(String alias);


    /**
     * Binding alias
     *
     * @param alias Account
     */
    void unBindAlias(String alias);


    /**
     * Set up the tag
     *
     * @param tag   Tag
     */
    void addTag(String tag);


    /**
     * Delete up the tag
     *
     * @param tag   Tag
     */
    void deleteTag(String tag);

}
