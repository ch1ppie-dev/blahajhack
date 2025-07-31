package me.eternadox.client.impl.ui.altmanager;

import java.io.IOException;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

public class GuiAltManager extends GuiScreen
{
    private final GuiScreen parentScreen;
    private GuiPasswordField passwordField;
    private GuiTextField usernameField;


    public GuiAltManager(GuiScreen screen)
    {
        this.parentScreen = screen;

    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        this.usernameField.updateCursorCounter();
        this.passwordField.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, I18n.format("Log In", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 18, I18n.format("gui.cancel", new Object[0])));
        this.usernameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 66, 200, 20, EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY );
        this.usernameField.setFocused(true);

        this.passwordField = new GuiPasswordField(1, this.fontRendererObj, this.width / 2 - 100, 106, 200, 20, EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY);

        this.passwordField.setMaxStringLength(128);

    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 2)
            {


            }
            else if (button.id == 1)
            {
                this.mc.displayGuiScreen(null);
            }
            else if (button.id == 0)
            {
                if (this.passwordField.getText().isEmpty()){
                    this.mc.session = new Session(this.usernameField.getText(), "", "", "mojang");
                } else {
                    System.out.println("logging in w/microsoft");
                    String email;
                    String password;

                    if (this.usernameField.getText().split(":").length > 1){
                        email = this.usernameField.getText().split(":")[0];
                        password = this.usernameField.getText().split(":")[1];

                    } else {
                        email = usernameField.getText();
                        password = passwordField.getText();
                    }

                    MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                    MicrosoftAuthResult result;
                    try {
                        result = authenticator.loginWithCredentials(email, password);
                    } catch (MicrosoftAuthenticationException e){
                        System.out.println(e.getMessage());

                        return;
                    }

                    this.mc.session = new Session(result.getProfile().getName(), result.getProfile().getId(), result.getAccessToken(), "mojang");

                    System.out.println("Logged in as " + result.getProfile().getName() + " (" + result.getProfile().getId() + ")");
                }

            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.usernameField.textboxKeyTyped(typedChar, keyCode);
        this.passwordField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 15)
        {
            this.usernameField.setFocused(!this.usernameField.isFocused());
            this.passwordField.setFocused(!this.passwordField.isFocused());
        }

        if (keyCode == 28 || keyCode == 156)
        {
            this.actionPerformed((GuiButton)this.buttonList.get(0));
        }

    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Alt Manager", this.width / 2, 17, 16777215);
        this.drawString(this.fontRendererObj, EnumChatFormatting.ITALIC + "Username / E-Mail", this.width / 2 - 100, 53, -1);
        this.drawString(this.fontRendererObj, EnumChatFormatting.ITALIC + "Password", this.width / 2 - 100, 94, -1);
        this.usernameField.drawTextBox();
        this.passwordField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
