package org.aub.telegram.bot.stats;

public class Summary {
    private int people;
    private int request;
    private int avgRequestsPerPerson;
    private int minRequestsPerPerson;
    private int maxRequestsPerPerson;

    public Summary(int people, int request, int avgRequestsPerPerson, int minRequestsPerPerson, int maxRequestsPerPerson) {
        this.people = people;
        this.request = request;
        this.avgRequestsPerPerson = avgRequestsPerPerson;
        this.minRequestsPerPerson = minRequestsPerPerson;
        this.maxRequestsPerPerson = maxRequestsPerPerson;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "people=" + people +
                ", request=" + request +
                ", avgRequestsPerPerson=" + avgRequestsPerPerson +
                ", minRequestsPerPerson=" + minRequestsPerPerson +
                ", maxRequestsPerPerson=" + maxRequestsPerPerson +
                '}';
    }
}
