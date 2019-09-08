package com.studentportal.api;

import com.studentportal.assignments.*;
import com.studentportal.commands.CreateAssignmentCommand;
import com.studentportal.commands.GetAssignmentsByCourseIdCommand;
import com.studentportal.commands.SubmitCompletedQuizCommand;
import com.studentportal.hibernate.AssignmentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/assignment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AssignmentApi {
    private AssignmentService aService = new AssignmentService();

    @POST
    @Path("/project/create")
    public void createProject(String json) {
        ProjectAssignment a = (ProjectAssignment) AssignmentHelper.extractAssignmentFromJson(json);
        CreateAssignmentCommand cmd = new CreateAssignmentCommand(a, aService);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        apiControl.doWork();
    }

    @POST
    @Path("/quiz/create")
    public void createQuiz(String json) {
        QuizAssignment a = (QuizAssignment) AssignmentHelper.extractAssignmentFromJson(json);
        CreateAssignmentCommand cmd = new CreateAssignmentCommand(a, aService);

        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        apiControl.doWork();
    }

    @GET
    @Path("/by/{courseId}")
    public List<Assignment> getAssignmentsByCourseId(@PathParam("courseId") int courseId) {
        GetAssignmentsByCourseIdCommand cmd = new GetAssignmentsByCourseIdCommand(aService, courseId);

        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        return (List<Assignment>) apiControl.doWork();
    }

    @POST
    @Path("/complete/quiz")
    public String completeQuiz(String json) {
        CompletedQuiz compQuiz = AssignmentHelper.extractCompletedQuizFromJson(json);
        QuizGrader grader = new QuizGrader(aService, compQuiz);
        SubmitCompletedQuizCommand cmd = new SubmitCompletedQuizCommand(grader, aService);

        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        return (String) apiControl.doWork();
    }
}
