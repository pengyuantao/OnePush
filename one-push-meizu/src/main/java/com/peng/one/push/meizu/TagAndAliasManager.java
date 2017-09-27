package com.peng.one.push.meizu;

import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import java.util.List;

/**
 * 标签和别名的管理器
 * Created by pengyuantao on 2017/9/25 15:03.
 */

public class TagAndAliasManager {

  private static class SingleHolder {
    private static final TagAndAliasManager instance = new TagAndAliasManager();
  }

  //别名
  private String alias = "";
  //标签
  private List<SubTagsStatus.Tag> tagList;

  public static TagAndAliasManager getInstance() {
    return SingleHolder.instance;
  }

  public String getAlias() {
    return alias;
  }

  public List<SubTagsStatus.Tag> getTagList() {
    return tagList;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public void setTagList(List<SubTagsStatus.Tag> tagList) {
    this.tagList = tagList;
  }

}
