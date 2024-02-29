package com.rbc.ufa.Game.Stages;

import com.rbc.gunfight.GunFight;
import com.rbc.ufa.Map.Team.Team;
import com.rbc.ufa.Map.Team.UFAPlayer;
import com.rbc.ufa.Map.UFAMap;
import com.rbc.ufa.Tools.Tool;
import com.rbc.ufa.Tools.TpTeam;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.math.BigDecimal;
import java.util.*;

public class Settlement extends StageRunnable {
    private Integer[] maxScores;
    private int[] scores;
    private int plusScore;
    private int score;
    private String teamNames = "";

    public Settlement(UFAMap map) {
        super(map);
    }

    @Override
    public void start() {
        refreshBossBar();
        runTaskTimer(GunFight.getPlugin(GunFight.class), 0L, 0L);
        bossBar = Bukkit.createBossBar(
                map.getMessage().getGame("message.settlement_bossbar", map),
                BarColor.BLUE, BarStyle.SOLID);
        maxScores = new Integer[map.getTeams().getTeams().size()];
        scores = new int[map.getTeams().getTeams().size()];
        firstRun();
    }

    public void firstRun() {
        for (int num = 0; num < maxScores.length; num++) {
            maxScores[num] = map.getTeams().getTeams().get(num).getScore();
        }
        allTime = time = Collections.max(Arrays.asList(maxScores));
        plusScore = allTime <= map.getConfig().getSettlementTime() * 20 ? 1 : allTime / (map.getConfig().getSettlementTime() * 20);
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            TpTeam.randomPrepare(ufaPlayer);
        }
        for (Team team : map.getTeams().getTeams()) {
            teamNames = teamNames.concat(
                    teamNames.isEmpty() ?
                            team.getName() :
                            map.getMessage().getGame("message.settlement_partition_title", map)
                                    .concat(team.getName()));
        }
    }

    @Override
    public void run() {
        if (time != 0) {
            time -= plusScore;
        }
        time = Math.max(0, time);
        float show = Math.max(0.0f, Math.min(1.0f, (allTime * 1.0f - time) / allTime));
        bossBar.setProgress(allTime != 0 ? show : 1.0f);
        score += plusScore;

        map.getUfaController().sendTitle(teamNames, getScoreStr(), 0, 20, 0);
        map.getUfaController().playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);

        super.run();
        if (time == 0) {
            ArrayList<Team> wins = new ArrayList<>();
            String winsStr = getWins(wins);
            if (wins.size() != maxScores.length) {
                rewardWinAndLose(wins, winsStr);
            } else {
                rewardDraw();
            }
            showInfo();
        }
    }

    private String getScoreStr() {
        String scoreStr = "";
        for (int num = 0; num < scores.length; num++) {
            scores[num] = Math.min(maxScores[num], score);
            scoreStr = scoreStr.concat(
                    scoreStr.isEmpty() ?
                            String.valueOf(scores[num]) :
                            map.getMessage().getGame("message.settlement_partition_subtitle", map)
                                    .concat(String.valueOf(scores[num])));
        }
        return scoreStr;
    }

    private String getWins(ArrayList<Team> wins) {
        String winsStr = "";
        for (int i : Tool.getSubscriptList(Arrays.asList(maxScores), Collections.max(Arrays.asList(maxScores)))) {
            wins.add(map.getTeams().getTeams().get(i));
            winsStr = winsStr.concat(
                    winsStr.isEmpty() ?
                            map.getTeams().getTeams().get(i).getName() :
                            map.getMessage().getGame("message.settlement_partition_subtitle", map)
                                    .concat(map.getTeams().getTeams().get(i).getName()));
        }
        return winsStr;
    }

    private void showInfo() {
        HashMap<Team, Integer> ts = new HashMap<>();
        for (Team team : map.getTeams().getTeams()) {
            ts.put(team, team.getScore());
        }
        List<Map.Entry<Team, Integer>> tsl = new ArrayList<>(ts.entrySet());
        tsl.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        for (Map.Entry<Team, Integer> me : tsl) {
            showPlayer(me.getKey());
        }
    }

    private void showPlayer(Team team) {
        HashMap<UFAPlayer, Integer> tp = new HashMap<>();
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            if (ufaPlayer.getTeam().equals(team)) {
                tp.put(ufaPlayer, ufaPlayer.getScore());
            }
        }
        List<Map.Entry<UFAPlayer, Integer>> tsl = new ArrayList<>(tp.entrySet());
        tsl.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        for (Map.Entry<UFAPlayer, Integer> me : tsl) {
            UFAPlayer ufaPlayer = me.getKey();
            map.getUfaController().sendMessage(
                    map.getMessage().getGame("info.settlement_gamer", map)
                            .replace("{team}", ufaPlayer.getTeam().getName())
                            .replace("{player}", ufaPlayer.getPlayer().getName())
                            .replace("{score}", String.valueOf(ufaPlayer.getScore()))
                            .replace("{kill}", String.valueOf(ufaPlayer.getKill()))
                            .replace("{die}", String.valueOf(ufaPlayer.getDie()))
                            .replace("{kd}", ufaPlayer.getKD()));
        }
    }

    @Override
    protected void setBossBar() {
        bossBar.setProgress(Math.min(1.0f, allTime != 0 ? (allTime * 1.0f - time) / allTime : 0.0f));
        bossBar.setTitle(
                map.getMessage().getGame("message.game_bossbar", map)
                        .replace("{time}", String.valueOf(time)));
    }

    public void rewardWinAndLose(ArrayList<Team> wins, String winsStr) {
        int rewardMultiple = map.getConfig().getRewardMultiple();
        ArrayList<Double> rewardMultipleList = map.getConfig().getRewardMultipleList();
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            if (wins.contains(ufaPlayer.getTeam())) {
                BigDecimal rewardMoney = new BigDecimal(ufaPlayer.getScore())
                        .multiply(new BigDecimal(rewardMultiple))
                        .multiply(BigDecimal.valueOf(rewardMultipleList.get(0)));
                ufaPlayer.getPlayer().sendTitle(
                        map.getMessage().getGame("message.settlement_win_title", map),
                        map.getMessage().getGame("message.settlement_wins_subtitle", map)
                                .replace("{wins}", winsStr)
                        , 20, 40, 20);
                ufaPlayer.getPlayer().sendMessage(
                        map.getMessage().getGame("message.settlement_reward", map)
                                .replace("{score}", String.valueOf(ufaPlayer.getScore()))
                                .replace("{rewardmultiple}", String.valueOf(rewardMultiple))
                                .replace("{rewardmultiplelist}", String.valueOf(rewardMultipleList.get(0)))
                                .replace("{money}", rewardMoney.toString()));
                Tool.reward(ufaPlayer.getPlayer(), rewardMoney, map);
            } else {
                BigDecimal rewardMoney = new BigDecimal(ufaPlayer.getScore())
                        .multiply(new BigDecimal(rewardMultiple))
                        .multiply(BigDecimal.valueOf(rewardMultipleList.get(2)));
                ufaPlayer.getPlayer().sendTitle(
                        map.getMessage().getGame("message.settlement_lose_title", map),
                        map.getMessage().getGame("message.settlement_wins_subtitle", map)
                                .replace("{wins}", winsStr)
                        , 20, 40, 20);
                ufaPlayer.getPlayer().sendMessage(
                        map.getMessage().getGame("message.settlement_reward", map)
                                .replace("{score}", String.valueOf(ufaPlayer.getScore()))
                                .replace("{rewardmultiple}", String.valueOf(rewardMultiple))
                                .replace("{rewardmultiplelist}", String.valueOf(rewardMultipleList.get(2)))
                                .replace("{money}", rewardMoney.toString()));
                Tool.reward(ufaPlayer.getPlayer(), rewardMoney, map);
            }
        }
        for (UFAPlayer ufaPlayer : map.getUfaController().getOtherPlayers()) {
            ufaPlayer.getPlayer().sendMessage(
                    map.getMessage().getGame("message.settlement_wins_subtitle", map)
                            .replace("{wins}", winsStr));
        }
    }

    public void rewardDraw() {
        int rewardMultiple = map.getConfig().getRewardMultiple();
        ArrayList<Double> rewardMultipleList = map.getConfig().getRewardMultipleList();
        for (UFAPlayer ufaPlayer : map.getUfaController().getGamePlayers()) {
            ufaPlayer.getPlayer().sendTitle(
                    map.getMessage().getGame("message.settlement_draw_title", map),
                    null
                    , 20, 40, 20);
            BigDecimal rewardMoney = new BigDecimal(ufaPlayer.getScore())
                    .multiply(new BigDecimal(rewardMultiple))
                    .multiply(BigDecimal.valueOf(rewardMultipleList.get(1)));
            ufaPlayer.getPlayer().sendMessage(
                    map.getMessage().getGame("message.settlement_reward", map)
                            .replace("{score}", String.valueOf(ufaPlayer.getScore()))
                            .replace("{rewardmultiple}", String.valueOf(rewardMultiple))
                            .replace("{rewardmultiplelist}", String.valueOf(rewardMultipleList.get(1)))
                            .replace("{money}", rewardMoney.toString()));
            Tool.reward(ufaPlayer.getPlayer(), rewardMoney, map);
        }

        for (UFAPlayer ufaPlayer : map.getUfaController().getOtherPlayers()) {
            ufaPlayer.getPlayer().sendMessage(
                    map.getMessage().getGame("message.settlement_draw_title", map));
        }

    }
}
