package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.PremierLeagueException;
import models.FootballClub;
import models.Match;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import requests.AddFootballClubRequest;
import requests.AddMatchRequest;
import responses.ErrorResponse;
import responses.LeagueTableResponse;
import responses.MatchResponse;
import responses.StatsResponse;
import services.PremierLeagueManager;
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

public class PremierLeagueController {

    private HttpExecutionContext httpExecutionContext;

    @Inject
    PremierLeagueManager premierLeagueManager;

    @Inject
    public PremierLeagueController(HttpExecutionContext httpExecutionContext){
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
                premierLeagueManager.createSeason(seasonYear);
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Season Created Successfully."));
            } catch (PremierLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());
    }

    public CompletionStage<Result> listSeasons(Http.Request request){

        return supplyAsync(()->{
            List<String> res = premierLeagueManager.listSeasons();
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
                premierLeagueManager.createAndAddFootballClub(seasonYear, Json.fromJson(json, AddFootballClubRequest.class));
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Club Created Successfully."));
            } catch (PremierLeagueException e) {
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
                premierLeagueManager.deleteFootballClub(seasonYear, name);
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Club Deleted Successfully."));
            } catch (PremierLeagueException e) {
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
                StatsResponse response = premierLeagueManager.displayStats(seasonYear, name);
                return ok(Json.toJson(response));
            } catch (PremierLeagueException e) {
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
                List<LeagueTableResponse> response = premierLeagueManager.displayPremierLeagueTable(seasonYear);
                return ok(Json.toJson(response));
            } catch (PremierLeagueException e) {
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
                premierLeagueManager.addMatch(seasonYear, Json.fromJson(json, AddMatchRequest.class));
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Match Successfully Added."));
            } catch (PremierLeagueException e) {
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
                premierLeagueManager.testLoad(seasonYear);
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Succesfully Loaded"));
            } catch (PremierLeagueException e) {
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
                Set<String> footballClubs = premierLeagueManager.getFootBallClubs(seasonYear);
                return ok(Json.toJson(footballClubs));
            } catch (PremierLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> loadState(Http.Request request){

       return supplyAsync(()->{
           try {
               premierLeagueManager.loadState();
               return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCESS", "State Loaded Successfully"));
           } catch (PremierLeagueException e) {
               return badRequest(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
           }
       }, httpExecutionContext.current());

    }

    public CompletionStage<Result> saveState(Http.Request request){

        return supplyAsync(()->{

            try {
                premierLeagueManager.saveState();
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCESS", "State Saved Successfully"));
            } catch (PremierLeagueException e) {
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
                premierLeagueManager.addRandomMatch(seasonYear);
                return ok(PremierLeagueUtil.returnJsonGeneralResponse("SUCESS", "Match Added Successfully"));
            } catch (PremierLeagueException e) {
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
                List<MatchResponse> matches = premierLeagueManager.listMatches(seasonYear);
                if(matches.isEmpty()){
                    return badRequest(PremierLeagueUtil.returnJsonError("-1", "No Matches Has Been Entered"));
                }else{
                    return ok(Json.toJson(matches));
                }
            } catch (PremierLeagueException e) {
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
                Match match = premierLeagueManager.getMatchFromDate(seasonYear, date);

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
            } catch (PremierLeagueException e) {
                return badRequest(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

}
