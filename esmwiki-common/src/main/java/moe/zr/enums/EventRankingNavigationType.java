package moe.zr.enums;

public enum EventRankingNavigationType {
    R1(1),
    R100(100),
    R1000(1000),
    R5000(5000),
    R10000(10000),
    R50000(50000);

    private final int rank;

    EventRankingNavigationType(int rank) {
        this.rank = rank;
    }


    public int getRank(){
        return rank;
    }
}
