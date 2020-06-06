package main.client;

public class  LoginErrorCommand implements Command {
    private LoginPanelView view;
    public LoginErrorCommand(LoginPanelView view) {
        this.view = view;
    }

    @Override
    public void execute() {
        view.setVisible(false);
        view.getMainPanel().add(view.getErrorLabel()); //метка появится в основной панели
        view.getErrorLabel().setVisible(true);
        view.setVisible(true);
        view.repaint();
    }
}
