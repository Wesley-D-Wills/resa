package resa.evaluation.topology.vld;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import resa.evaluation.topology.tomVLD.StormConfigManager;
import resa.topology.ResaTopologyBuilder;
import resa.util.ConfigUtil;
import resa.util.ResaConfig;

import static resa.util.ConfigUtil.getInt;

/**
 * Created by ding on 14-7-3.
 * This beta version is Modified by Tom Fu, on April 2016
 * We mainly re-design the topology to remove those broadcasting issue (all grouping), here for experimental purpose
 */
public class DectationTopologyDelta2 implements Constant {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Enter path to config file!");
            System.exit(0);
        }
        Config conf = StormConfigManager.readConfig(args[0]);

        TopologyBuilder builder = new ResaTopologyBuilder();

        String host = (String) conf.get("redis.host");
        int port = ConfigUtil.getInt(conf, "redis.port", 6379);
        String queue = (String) conf.get("redis.queue");

        builder.setSpout("image-input", new ImageSource(host, port, queue), getInt(conf, "vd.spout.parallelism", 1));

        builder.setBolt("feat-ext", new FeatureExtracterDelta2(), getInt(conf, "vd.feat-ext.parallelism", 1))
                .shuffleGrouping("image-input", STREAM_IMG_OUTPUT)
                .setNumTasks(getInt(conf, "vd.feat-ext.tasks", 1));
        builder.setBolt("matcher", new MatcherDelta(), getInt(conf, "vd.matcher.parallelism", 1))
                .shuffleGrouping("feat-ext", STREAM_FEATURE_DESC)
                .setNumTasks(getInt(conf, "vd.matcher.tasks", 1));
        builder.setBolt("vd-aggregator", new AggregaterDelta(), getInt(conf, "vd.aggregator.parallelism", 1))
                .fieldsGrouping("matcher", STREAM_MATCH_IMAGES, new Fields(FIELD_FRAME_ID))
                .setNumTasks(getInt(conf, "vd.aggregator.tasks", 1));

        int numWorkers = ConfigUtil.getInt(conf, "vd-worker.count", 1);
        conf.setNumWorkers(numWorkers);
        conf.setMaxSpoutPending(ConfigUtil.getInt(conf, "vd-MaxSpoutPending", 0));
        conf.setStatsSampleRate(1.0);

        ResaConfig resaConfig = ResaConfig.create();
        resaConfig.putAll(conf);

        if (ConfigUtil.getBoolean(conf, "vd.metric.resa", false)) {
            resaConfig.addDrsSupport();
            resaConfig.put(ResaConfig.REBALANCE_WAITING_SECS, 0);
            System.out.println("ResaMetricsCollector is registered");
        }

        StormSubmitter.submitTopology("resa-vd-JB-d2", resaConfig, builder.createTopology());

    }

}