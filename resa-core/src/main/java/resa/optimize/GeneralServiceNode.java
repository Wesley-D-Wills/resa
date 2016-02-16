package resa.optimize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tom.fu on 23/4/2014.
 */
public class GeneralServiceNode {

    private static final Logger LOG = LoggerFactory.getLogger(GeneralServiceNode.class);

    private String componentID;
    private int executorNumber;
    private double compSampleRate;

    private double avgSendQueueLength;
    private double avgRecvQueueLength;

    private double avgServTimeHis;
    private double scvServTimeHis;
    private double mu;

    private double numCompleteTuples;
    private double sumDurationSeconds;
    private double tupleCompleteRate;

    /*metrics on recv_queue*/
    private double lambda;
    private double lambdaByInterArrival;
    private double interArrivalScv;

    private double exArrivalRate;
    private double exArrivalRateByInterArrival;

    private double ratio;
    private double ratioByInterArrival;

    double rho = lambda / (executorNumber * mu);
    double rhoBIA = lambdaByInterArrival / (executorNumber * mu);

    public GeneralServiceNode(
            String componentID,
            int executorNumber,
            double compSampleRate,
            double avgSendQueueLength,
            double avgRecvQueueLength,
            double avgServTimeHis,
            double scvServTimeHis,
            double numCompleteTuples,
            double sumDurationSeconds,
            double tupleCompleteRate,
            double lambda,
            double lambdaByInterArrival,
            double interArrivalScv,
            double exArrivalRate,
            double exArrivalRateByInterArrival) {
        this.componentID = componentID;
        this.executorNumber = executorNumber;
        this.compSampleRate = compSampleRate;
        this.avgSendQueueLength = avgSendQueueLength;
        this.avgRecvQueueLength = avgRecvQueueLength;
        this.avgServTimeHis = avgServTimeHis;
        this.scvServTimeHis = scvServTimeHis;
        this.numCompleteTuples = numCompleteTuples;
        this.sumDurationSeconds = sumDurationSeconds;
        this.tupleCompleteRate = tupleCompleteRate;
        this.lambda = lambda;
        this.lambdaByInterArrival = lambdaByInterArrival;
        this.interArrivalScv = interArrivalScv;
        this.exArrivalRate = exArrivalRate;
        this.exArrivalRateByInterArrival = exArrivalRateByInterArrival;

        this.mu = this.avgServTimeHis > 0.0 ? (1000.0 / this.avgServTimeHis) : Double.MAX_VALUE;
        this.ratio = this.exArrivalRate > 0.0 ? (this.lambda / this.exArrivalRate) : 0;
        this.ratioByInterArrival = this.exArrivalRateByInterArrival > 0.0 ? (this.lambdaByInterArrival / this.exArrivalRateByInterArrival) : 0;

        rho = this.lambda / (this.executorNumber * mu);
        rhoBIA = this.lambdaByInterArrival / (this.executorNumber * mu);

        LOG.info(toString());
    }

    @Override
    public String toString() {

        return String.format(
                "Component(ID, eNum):(%s,%d), tupleProcCnt: %d, sumMeasuredDur: %.1f, sampleRate: %.1f, tupleProcRate: %.3f, " +
                        "avgSendQLen: %.1f, avgRecvQLen: %.1f, avgServTimeMS: %.4f, scvServTime: %.4f, mu: %.4f" +
                        "arrRateHis: %.4f, arrRateBIA: %.4f, interArrivalScv: %.4f " +
                        "ratio: %.4f, ratioBIA: %.4f, rho: %.4f, rhoBIA: %.4f",
                componentID, executorNumber, numCompleteTuples, sumDurationSeconds, compSampleRate, tupleCompleteRate,
                avgSendQueueLength, avgRecvQueueLength, avgServTimeHis, scvServTimeHis, mu,
                lambda, lambdaByInterArrival, interArrivalScv,
                ratio, ratioByInterArrival, rho, rhoBIA);
    }

    public String getComponentID() {
        return componentID;
    }

    public int getExecutorNumber() {
        return executorNumber;
    }

    public double getCompSampleRate() {
        return compSampleRate;
    }

    public double getAvgSendQueueLength() {
        return avgSendQueueLength;
    }

    public double getAvgRecvQueueLength() {
        return avgRecvQueueLength;
    }

    public double getAvgServTimeHis() {
        return avgServTimeHis;
    }

    public double getScvServTimeHis() {
        return scvServTimeHis;
    }

    public double getNumCompleteTuples() {
        return numCompleteTuples;
    }

    public double getSumDurationSeconds() {
        return sumDurationSeconds;
    }

    public double getTupleCompleteRate() {
        return tupleCompleteRate;
    }

    public double getLambda() {
        return lambda;
    }

    public double getLambdaByInterArrival() {
        return lambdaByInterArrival;
    }

    public double getInterArrivalScv() {
        return interArrivalScv;
    }

    public double getExArrivalRate() {
        return exArrivalRate;
    }

    public double getExArrivalRateByInterArrival() {
        return exArrivalRateByInterArrival;
    }

    public double getMu() {
        return mu;
    }

    public double getRatio() {
        return ratio;
    }

    public double getRatioByInterArrival() {
        return ratioByInterArrival;
    }

    public double getRho() {
        return rho;
    }

    public double getRhoBIA() {
        return rhoBIA;
    }
}
