package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.PremierLeagueException;
import exceptions.UniversityLeagueException;
import exceptions.UniversityLeagueException;
import models.Match;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import requests.AddFootballClubRequest;
import requests.AddMatchRequest;
import responses.LeagueTableResponse;
import responses.MatchResponse;
import responses.StatsResponse;
import responses.UniversityStatsResponse;
import services.UniversityLeagueManager;
import utils.PremierLeagueUtil;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.*;

public class UniversityLeagueController {

    private HttpExecutionContext httpExecutionContext;

    @Inject
    UniversityLeagueManager universityLeagueManager;

    @Inject
    public UniversityLeagueController(HttpExecutionContext httpExecutionContext){
        this.httpExecutionContext = httpExecutionContext;
    }

    public CompletionStage<Result> createSeason(String season, Http.Request request){
        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                universityLeagueManager.createSeason(seasonYear);
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Season Created Successfully."));
            } catch (UniversityLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> listSeasons(Http.Request request){

        return supplyAsync(()->{
            List<String> res = universityLeagueManager.listSeasons();
            if(!res.isEmpty()){
                return ok(Json.toJson(res));
            }
            else {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", "No Seasons Available"));
            }
        },httpExecutionContext.current());

    }

    public CompletionStage<Result> createPremierLeagueClub(String season, Http.Request request){
        JsonNode json = request.body().asJson();

        return supplyAsync(() ->{
            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            if(json == null){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Request Body is Empty");
                return badRequest(jsonNode);
            }

            try {
                universityLeagueManager.createAndAddFootballClub(seasonYear, Json.fromJson(json, AddFootballClubRequest.class));
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Club Created Successfully."));
            } catch (UniversityLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> deletePremietLeagueClub(String season, String name, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            if(name == null){
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", "Name Cannot Be Empty"));
            }
            if(name.isEmpty() || name.isBlank()){
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", "Name Cannot Be Empty"));
            }

            try {
                universityLeagueManager.deleteFootballClub(seasonYear, name);
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Club Deleted Successfully."));
            } catch (UniversityLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> displayStatsPremierLeagueClub(String season, String name, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            if(name == null){
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", "Name Cannot Be Empty"));
            }
            if(name.isEmpty() || name.isBlank()){
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", "Name Cannot Be Empty"));
            }

            try {
                UniversityStatsResponse response = universityLeagueManager.displayStats(seasonYear, name);
                return ok(Json.toJson(response));
            } catch (UniversityLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> displayPremierLeagueTable(String season, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                List<LeagueTableResponse> response = universityLeagueManager.displayPremierLeagueTable(seasonYear);
                return ok(Json.toJson(response));
            } catch (UniversityLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        },httpExecutionContext.current());

    }

    public CompletionStage<Result> addMatch(String season, Http.Request request){

        JsonNode json = request.body().asJson();

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            if(json == null){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Request Body is Empty");
                return badRequest(jsonNode);
            }

            try {
                universityLeagueManager.addMatch(seasonYear, Json.fromJson(json, AddMatchRequest.class));
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Match Successfully Added."));
            } catch (UniversityLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> testLoad(String season, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                universityLeagueManager.testLoad(seasonYear);
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Succesfully Loaded"));
            } catch (UniversityLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> getFootballClubs(String season, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                Set<String> footballClubs = universityLeagueManager.getFootBallClubs(seasonYear);
                return ok(Json.toJson(footballClubs));
            } catch (UniversityLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> loadState(Http.Request request){

        return supplyAsync(()->{
            try {
                universityLeagueManager.loadState();
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCESS", "State Loaded Successfully"));
            } catch (UniversityLeagueException e) {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }
        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> saveState(Http.Request request){

        return supplyAsync(()->{

            try {
                universityLeagueManager.saveState();
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCESS", "State Saved Successfully"));
            } catch (UniversityLeagueException e) {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> addRandomMatch(String season, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                universityLeagueManager.addRandomMatch(seasonYear);
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCESS", "Match Added Successfully"));
            } catch (UniversityLeagueException e) {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        });

    }

    public CompletionStage<Result> listMatches(String season, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                List<MatchResponse> matches = universityLeagueManager.listMatches(seasonYear);
                if(matches.isEmpty()){
                    return badRequest(PremierLeagueUtil.returnJsonError("-1", "No Matches Has Been Entered"));
                }else{
                    return ok(Json.toJson(matches));
                }
            } catch (Exception e) {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        });

    }

    public CompletionStage<Result> getMatchFromDate(String season, String date, Http.Request request){

        return supplyAsync(()->{

            String seasonYear = season;
            try{
                int seasonInt = Integer.parseInt(seasonYear);
                seasonYear = PremierLeagueUtil.getSeasonText(seasonInt);
            }
            catch (Exception e){
                JsonNode jsonNode = PremierLeagueUtil.returnJsonError("-1", "Invalid Season");
                return badRequest(jsonNode);
            }

            try {
                Match match = universityLeagueManager.getMatchFromDate(seasonYear, date);

                MatchResponse matchResponse = new MatchResponse();
                matchResponse.setTeam1(match.getTeam1().getName());
                matchResponse.setTeam2(match.getTeam2().getName());
                matchResponse.setScore1(String.valueOf(match.getTeam1Score()));
                matchResponse.setScore2(String.valueOf(match.getTeam2Score()));

                LocalDate localdate = match.getDateTime().toLocalDate();
                LocalTime time = match.getDateTime().toLocalTime();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                matchResponse.setDate(localdate.format(formatter));
                formatter = DateTimeFormatter.ofPattern("HH:mm");
                matchResponse.setTime(time.format(formatter));
                matchResponse.setStatus(match.getStatus());

                return ok(Json.toJson(matchResponse));
            } catch (UniversityLeagueException e) {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

}
