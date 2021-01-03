package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import exceptions.CommonServiceException;
import exceptions.PremierLeagueException;
import exceptions.SchoolLeagueException;
import exceptions.UniversityLeagueException;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import services.CommonService;
import services.PremierLeagueManager;
import services.SchoolLeagueManager;
import services.UniversityLeagueManager;
import utils.PremierLeagueUtil;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.*;

public class CommonController {

    private HttpExecutionContext httpExecutionContext;

    @Inject
    SchoolLeagueManager schoolLeagueManager;

    @Inject
    PremierLeagueManager premierLeagueManager;

    @Inject
    UniversityLeagueManager universityLeagueManager;

    @Inject
    CommonService commonService;

    @Inject
    public CommonController(HttpExecutionContext httpExecutionContext){
        this.httpExecutionContext = httpExecutionContext;
    }

    public CompletionStage<Result> createSeasons(String season, Http.Request request){

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
                schoolLeagueManager.createSeason(seasonYear);
                premierLeagueManager.createSeason(seasonYear);
                universityLeagueManager.createSeason(seasonYear);
                return created(PremierLeagueUtil.returnJsonGeneralResponse("SUCCESS", "Season Created Successfully."));
            } catch (SchoolLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            } catch (UniversityLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            } catch (PremierLeagueException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        }, httpExecutionContext.current());

    }

    public CompletionStage<Result> listSeasons(Http.Request request){

        return supplyAsync(()->{

            try {
                Set<String> res = commonService.listSeasons();
                return ok(Json.toJson(res));
            } catch (CommonServiceException e) {
                return notAcceptable(PremierLeagueUtil.returnJsonError("-1", e.getMessage()));
            }

        });

    }


}
