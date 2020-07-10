package epam.eremenko.restaurant.controller.command;

public enum CCMD {

//    CHANGE_LOCALE(((request, response) -> {
//        request.getSession(true).setAttribute("local", request.getParameter("local"));
//        response.sendRedirect(request.getContextPath() + "/restaurant");
//    }));

    CHANGE_LOCAL(new UserCommand().LOCAL_CHANGER);

    private final Command command;

    CCMD(Command command){
        this.command= command;
    }

    public Command getCommand() {
        return command;
    }
}
