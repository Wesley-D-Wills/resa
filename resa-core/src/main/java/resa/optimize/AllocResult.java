package resa.optimize;

import java.util.Map;

/**
 * Created by ding on 14-4-29.
 * Modified by Tom Fu on Feb-16-2016
 *
 * in this modification, we consider three sets of allocation results:
 * a) minReqOptAllocation given QoS input
 * b) kMaxOptAllocation given K_max available number of executors
 * c) currOptAllocation, the optimal allocation based on the currently used number of executors
 * d) Status tells if any operator is stable (rho < 1) or unstable (rho > 1)
 */
public class AllocResult {

    //TODO: add expected QoS for both minReqOptAllocation and currOptAllocation
    //so that for later programme to optimize the rebalance behavior
    // (e.g. consider expected rebalance gain vs. cost)

    public static enum Status {
        INFEASIBLE, FEASIBLE
    }

    public final Status status;
    public final Map<String, Integer> minReqOptAllocation;
    public final Map<String, Integer> kMaxOptAllocation;
    public final Map<String, Integer> currOptAllocation;
    private Object context = null;

    public AllocResult(Status status, Map<String, Integer> minReqOptAllocation,
                       Map<String, Integer> kMaxOptAllocation, Map<String, Integer> currOptAllocation) {
        this.status = status;
        this.minReqOptAllocation = minReqOptAllocation;
        this.kMaxOptAllocation = kMaxOptAllocation;
        this.currOptAllocation = currOptAllocation;
    }

    public AllocResult(Status status, Map<String, Integer> currOptAllocation) {
        this(status, null, null, currOptAllocation);
    }

    public AllocResult(Status status, Map<String, Integer> minReqOptAllocation, Map<String, Integer> currOptAllocation) {
        this(status, minReqOptAllocation, null, currOptAllocation);
    }

    public Object getContext() {
        return context;
    }

    public AllocResult setContext(Object context) {
        this.context = context;
        return this;
    }
}
