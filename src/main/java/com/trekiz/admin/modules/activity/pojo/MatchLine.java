package com.trekiz.admin.modules.activity.pojo;

/**
 * 用于返回查询出的匹配路线的JsonBean.
 * @author  yudong.xu 2016.10.13
 */
public class MatchLine{

    private Long touristLineId; // 线路的id,对应TouristLine对象的id

    private String touristLineName; // 线路的名称,对应TouristLine对象的lineName

    private Integer hitScore; // 用于比较的命中得分,命中个数


    public MatchLine() { }

    public MatchLine(Long touristLineId, String touristLineName, Integer hitScore) {
        this.touristLineId = touristLineId;
        this.touristLineName = touristLineName;
        this.hitScore = hitScore;
    }

    public Long getTouristLineId() {
        return touristLineId;
    }

    public void setTouristLineId(Long touristLineId) {
        this.touristLineId = touristLineId;
    }

    public String getTouristLineName() {
        return touristLineName;
    }

    public void setTouristLineName(String touristLineName) {
        this.touristLineName = touristLineName;
    }

    public Integer getHitScore() {
        return hitScore;
    }

    public void setHitScore(Integer hitScore) {
        this.hitScore = hitScore;
    }
}
