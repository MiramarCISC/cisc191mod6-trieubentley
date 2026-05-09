package edu.sdccd.cisc191;

import java.util.*;
import java.util.stream.Collectors;

public class GameServerAnalytics {

    // Sort by rating highest to lowest, take top n, extract usernames
    public static List<String> findTopNUsernamesByRating(Collection<PlayerAccount> players, int n) {
        return players.stream()
                .sorted(Comparator.comparingInt(PlayerAccount::rating).reversed())
                .limit(n)
                .map(PlayerAccount::username)
                .collect(Collectors.toList());
    }

    // Group players by region, take average of the rating of each group
    public static Map<String, Double> averageRatingByRegion(Collection<PlayerAccount> players) {
        return players.stream()
                .collect(Collectors.groupingBy(
                        PlayerAccount::region,
                        Collectors.averagingInt(PlayerAccount::rating)
                ));
    }

    // Track "seen" usernames so that any username that fails to add to the seen set is a duplicate
    public static Set<String> findDuplicateUsernames(Collection<PlayerAccount> players) {
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (PlayerAccount player : players) {
            if (!seen.add(player.username())) {
                duplicates.add(player.username());
            }
        }
        return duplicates;
    }

    // Group players by tier, collecting only their usernames per group
    public static Map<String, List<String>> groupUsernamesByTier(Collection<PlayerAccount> players) {
        return players.stream()
                .collect(Collectors.groupingBy(
                        GameServerAnalytics::tierFor,
                        Collectors.mapping(PlayerAccount::username, Collectors.toList())
                ));
    }

    // append match summary to both players' lists
    public static Map<String, List<String>> buildRecentMatchSummariesByPlayer(Collection<MatchRecord> matches) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        for (MatchRecord match : matches) {
            String summary = match.summary();
            // computeIfAbsent creates a new list the first time a player appears
            result.computeIfAbsent(match.playerOne().username(), k -> new ArrayList<>()).add(summary);
            result.computeIfAbsent(match.playerTwo().username(), k -> new ArrayList<>()).add(summary);
        }
        return result;
    }

    // Return whichever item the comparator ranks higher
    public static <T> T pickHigherRated(T first, T second, Comparator<T> comparator) {
        return comparator.compare(first, second) > 0 ? first : second;
    }

    // Tier cutoffs
    public static String tierFor(PlayerAccount player) {
        if (player.rating() < 1000) return "Bronze";
        if (player.rating() < 1400) return "Silver";
        return "Gold";
    }
}