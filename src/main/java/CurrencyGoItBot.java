import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import settings.*;

public class CurrencyGoItBot extends TelegramLongPollingBot {
    public CurrencyGoItBot(DefaultBotOptions options) {
        super(options);
        Settings.readSettings();
    }


    @Override
    public String getBotUsername() {
        return "@CurrencyGoItBot";
    }

    @Override
    public String getBotToken() {
        return "5403460194:AAHy6snkFdJlGwXSrtmNDQlDYAQbf-fFaOo";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().isCommand()) {
            try {
                handleCommand(update);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            try {
                handleCallback(update.getCallbackQuery());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    private void handleCommand(Update update) throws TelegramApiException {
        Message message = update.getMessage();
        String textCommand = update.getMessage().getText();
        switch (textCommand) {
            case "/start":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????? ??????????????. ?????? ?????? ???????????????? ?????????????????????????????? ?????????????????? ?????????? ??????????.")
                        .replyMarkup(Button.getInitialButtons())
                        .build());
                break;
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) throws TelegramApiException {
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData();
        Long chatId = message.getChatId();
        switch (data) {
            case "buttonGetInfo":
                MakeOutputString makeOutputString = new MakeOutputString();
                String output = makeOutputString.processInfo(chatId);
                execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text(output)
                        .replyMarkup(Button.getInitialButtons())
                        .build());
                break;
            case "buttonSettings":
                execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("????????????????????????.")
                        .replyMarkup(Button.getSettingsButtons())
                        .build());
                break;
            case "buttonDigitsNumber":
                execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("???????????????? ?????????????????? ???????????? ?????????? ????????")
                        .replyMarkup(NumberSimbolsAfterCommaSetting.getDigitsButtons(chatId))
                        .build());
                break;
            case "buttonBank":
                execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("???????????????? ????????")
                        .replyMarkup(BankSetting.getBankButtons(chatId))
                        .build());
                break;
            case "buttonCurrencies":
                execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("???????????????? ????????????")
                        .replyMarkup(CurrencySetting.getCurrenciesButtons(chatId))
                        .build());
                break;
            case "buttonNotificationTime":
                execute(SendMessage.builder()
                        .chatId(chatId.toString())
                        .text("???????????????? ?????? ????????????????????")
                        .replyMarkup(NotificationSetting.getNotificationButtons(chatId))
                        .build());
                break;

            case "NBU":
            case "PRIVAT":
            case "MONO":
                Settings.bankSetting.setSavedBank(chatId, BankSetting.Bank.valueOf(callbackQuery.getData()));

                execute(EditMessageReplyMarkup.builder()
                        .chatId(chatId)
                        .messageId(message.getMessageId())
                        .replyMarkup(BankSetting.getBankButtons(chatId))
                        .build());
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("???????? ????????????")
                        .replyMarkup(Button.getReturnButton())
                        .build());
                Settings.writeSettings();
                break;

            case "TWO":
            case "THREE":
            case "FOUR":
                Settings.digitsSetting.setSimbolsAfterComma(chatId, NumberSimbolsAfterCommaSetting.NumberSimbolsAfterComma
                        .valueOf(callbackQuery.getData()));

                execute(EditMessageReplyMarkup.builder()
                        .chatId(chatId)
                        .messageId(message.getMessageId())
                        .replyMarkup(NumberSimbolsAfterCommaSetting.getDigitsButtons(chatId))
                        .build());
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ???????????????????? ???????????????? ??????????????????")
                        .replyMarkup(Button.getReturnButton())
                        .build());
                Settings.writeSettings();
                break;

            case "USD":
            case "EUR":
            case "GBP":
                Settings.currencySetting.setSavedCurrency(chatId, CurrencySetting.Currency.valueOf(callbackQuery.getData()));

                execute(EditMessageReplyMarkup.builder()
                        .chatId(chatId)
                        .messageId(message.getMessageId())
                        .replyMarkup(CurrencySetting.getCurrenciesButtons(chatId))
                        .build());
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("???????????? ????????????")
                        .replyMarkup(Button.getReturnButton())
                        .build());
                Settings.writeSettings();
                break;
            case "reset":
                execute(SendMessage.builder()
                        .chatId(chatId)
                        .text("?????????????? ?????????? ????????")
                        .replyMarkup(Button.getInitialButtons())
                        .build());
        }
    }

    private void handleMessage(Message message) throws TelegramApiException {
        String text = message.getText();
        switch (text) {
            case "??????????????????????":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????? ???????????? ??????????????.")
                        .replyMarkup(Button.getSettingsButtons())
                        .build());
                break;
            case "9":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 9 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.NINE);
                break;
            case "10":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 10 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.TEN);
                break;
            case "11":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 11 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.ELEVEN);
                break;
            case "12":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 12 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.TWELVE);
                break;
            case "13":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 13 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.THIRTEEN);
                break;
            case "14":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 14 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.FOURTEEN);
                break;
            case "15":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 15 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.FIFTEEN);
                break;
            case "16":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 16 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.SIXTEEN);
                break;
            case "17":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 17 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.SEVENTEEN);
                break;
            case "18":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("?????????????????? ?????? ???????????????????? ?? 18 ????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.EIGHTEEN);
                break;
            case "???????????????? ????????????????????":
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("???? ???????????????? ????????????????????.")
                        .build());
                NotificationSetting.setNotification(message.getChatId(), NotificationSetting.Notification.OFF_NOTIFY);
                break;
        }
    }

    public void sendNotification(long chatId) throws TelegramApiException {
        MakeOutputString makeOutputString = new MakeOutputString();
        execute(SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(makeOutputString.processInfo(chatId))
                .replyMarkup(Button.getInitialButtons())
                .build());
    }
}
