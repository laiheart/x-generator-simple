package dynamic;

import com.ection.platform.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lsx
 * @date 2024-08-01
 */
@Data
public class DynamicObj {

  /**
   * 表名
   */
  private String tableName;
  /**
   * 查询字段
   */
  private String select = "*";

  /**
   * 多行数据查询，使用 or 连接。(id = 1 and name='x') or (id=2 and name = 'y')
   */
  private List<Where> whereList;

  /**
   * 比如 id = 1 or name = 'y' and age = 18
   */
  private String cusWhere;

  private void setCusWhere(String cusWhere) {
  }

  private void setWhereList(List<Where> whereList) {

  }

  /**
   * 构造函数
   */
  public DynamicObj(String tableName) {
    this.tableName = tableName;
    whereList = new ArrayList<>(4);
  }

  /**
   * 创建查询条件
   *
   * @return
   */
  public Where createWhereAndAddList() {
    Where where = new Where();
    whereList.add(where);
    return where;
  }

  /**
   * 自定义where 里的内容
   *
   * @param ?为待填值
   */
  public void createCusWhere(String format, Object... values) {
    StringBuilder sb = new StringBuilder();
    String[] split = format.trim().split("\\?");
    if (split.length != values.length) {
      throw new BusinessException("where 条件数量与占位符数量不一致");
    }
    for (int i = 0; i < split.length; i++) {
      sb.append(split[i]).append(handlerValue(values[i]));
    }
    this.cusWhere = sb.toString();
  }

  /**
   * 格式自定 cusWhere 语句值
   *
   * @param value
   * @return
   */
  private static Object handlerValue(Object value) {
    if (value instanceof String) {
      return "'" + value + "'";
    } else if (value instanceof List) {
      return flatList((List<?>) value);
    } else if (value.getClass().isArray()) {
      return flatList(Arrays.asList((Object[]) value));
    }
    return value;
  }

  /**
   * 将条件添加到列表中
   *
   * @param where
   */
  public void addWhereList(Where where) {
    whereList.add(where);
  }

  /**
   * 查询条件对象 可添加多个查询条件，多个查询结果示例。id = 1 and name = 'x' or age = 18
   */
  @Data
  public static class Where {
    /**
     * 多个查询条件
     */
    private List<WhereItem> whereItemList;

    public Where() {
      whereItemList = new ArrayList<>(4);
    }

    /**
     * 添加查询条件
     *
     * @param whereItem
     * @return
     */
    public Where add(WhereItem whereItem) {
      whereItemList.add(whereItem);
      return this;
    }

    public Where andEqual(String column, Object value) {
      whereItemList.add(new WhereItem(column, "=", value, "and"));
      return this;
    }

    public Where andNotEqual(String column, Object value) {
      whereItemList.add(new WhereItem(column, "<>", value, "and"));
      return this;
    }

    public Where orEqual(String column, Object value) {
      whereItemList.add(new WhereItem(column, "=", value, "or"));
      return this;
    }

    public Where orNotEqual(String column, Object value) {
      whereItemList.add(new WhereItem(column, "<>", value, "or"));
      return this;
    }

    public Where andIn(String column, List<?> value) {
      whereItemList.add(new WhereItem(column, "in", flatList(value), "and"));
      return this;
    }

    public Where andNotIn(String column, List<?> value) {
      whereItemList.add(new WhereItem(column, "not in", flatList(value), "and"));
      return this;
    }

    public Where orIn(String column, List<?> value) {
      whereItemList.add(new WhereItem(column, "in", flatList(value), "or"));
      return this;
    }

    public Where orNotIn(String column, List<?> value) {
      whereItemList.add(new WhereItem(column, "not in", flatList(value), "or"));
      return this;
    }

    public Where like(String column, Object value, String orAnd, boolean leftLike, boolean rightLike) {
      WhereItem whereItem = new WhereItem(column, "like", value, orAnd);
      if (leftLike) value = "%" + value;
      if (rightLike) value += "%";
      whereItem.setValue(value);
      whereItemList.add(whereItem);
      return this;
    }

    public Where andLike(String column, Object value) {
      return like(column, value, "and", true, true);
    }

    public Where andLike(String column, Object value, boolean leftLike, boolean rightLike) {
      return like(column, value, "and", leftLike, rightLike);
    }

    public Where andLeftLike(String column, Object value) {
      return like(column, value, "and", true, false);
    }

    public Where andRightLike(String column, Object value) {
      return like(column, value, "and", false, true);
    }

    public Where orLike(String column, Object value) {
      return like(column, value, "or", true, true);
    }

    public Where orLike(String column, Object value, boolean leftLike, boolean rightLike) {
      return like(column, value, "or", leftLike, rightLike);
    }

    public Where orLeftLike(String column, Object value) {
      return like(column, value, "or", true, false);
    }

    public Where orRightLike(String column, Object value) {
      return like(column, value, "or", false, true);
    }


  }

  private static String flatList(List<?> list) {
    if (ObjectUtils.isEmpty(list)) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (Object value : list) {
      if (value instanceof String) {
        sb.append("'").append(value).append("'");
      } else if (value instanceof Number) {
        sb.append(value);
      } else {
        sb.append("'").append(value).append("'");
      }
      sb.append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(")");
    return sb.toString();
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class WhereItem {
    /**
     * 数据库字段名
     */
    private String column;
    /**
     * 查询条件
     */
    private String condition = "=";
    /**
     * 查询值
     */
    private Object value;
    /**
     * 两个字段查询关系，可取值：or，and
     */
    private String orAnd = "and";
  }

}
