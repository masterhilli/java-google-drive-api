package work;


import google.api.auth.AuthorizeService;
import work.billing.Setting.FileSettingReader;
import work.billing.Setting.FileSettings;
import work.billing.Spreadsheets.ProjectSummarySpreadsheetUpdater;
import work.billing.Spreadsheets.ProjectsheetToTrackTimeMapper;
import work.billing.Spreadsheets.Spreadsheet;
import work.billing.Timesheet.TrackedTime;
import work.billing.Timesheet.TrackedTimeAlreadyExistsException;
import work.billing.Timesheet.TrackedTimeSummary;

import java.io.IOException;

public class AppRunner {

    public static void main(String[] args) throws IOException {
<<<<<<< HEAD
        if (args.length != 2) {
            System.out.println("Please provide 2 args: <worksheetname> <pathToSettingsFile>");
            return;
        }
        String worksheetName = args[0];
        String pathToSettingFile = args[1];
        FileSettings fileSettings = FileSettingReader.ReadFileSettingsFromFile(pathToSettingFile);
=======
        if (args.length < 2) {
            System.out.println("Please provide 2 args: <worksheetname> <pathToSettingsFile>");
            return;
        }

        FileSettings settings = FileSettingReader.ReadFileSettingsFromFile(args[1]);

        createBillingSpreadsheet(args[0], settings);


        // no glue if we still will need them.
        //TestMethodsForSpreadSheets();
    }

    private static void createBillingSpreadsheet(String worksheetName, FileSettings settings) {
>>>>>>> exportSpreadsheet

        TrackedTimeSummary trackedTimeSum = new TrackedTimeSummary();
        for (String key : settings.importFileId) {
            Spreadsheet timeSheet = new Spreadsheet(key, worksheetName);
            TrackedTime timeTracked = ProjectsheetToTrackTimeMapper.createTrackedTimeFromSpreadsheet(
                    timeSheet, worksheetName, settings.getHourRateAsHashMapPerTeamMember());
            try {
                trackedTimeSum.addTrackedTime(timeTracked);
            } catch (TrackedTimeAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
        //trackedTimeSum.printTimesForAllProjects();
        //trackedTimeSum.printTimesForAllTeamMembers();
        int startPos = 10;
        for (String projectName : trackedTimeSum.getProjectNames()) {
            ProjectSummarySpreadsheetUpdater export = new ProjectSummarySpreadsheetUpdater(settings.exportFileId,
                    trackedTimeSum.receiveTrackedTimesPerProject(projectName));
            export.WriteProjectToSpreadSheet(startPos, worksheetName);
            startPos = export.getLastPosition() + 1;
        }
        System.out.println("finished update of field.");
    }

    private static void TestMethodsForSpreadSheets() {
        Spreadsheet mySpreadsheet = new Spreadsheet("1MBc1Uvv4Wfyw31mwoGnrEzfCaXxcd1BT-aLg0x1VS_Y");
        System.out.println(mySpreadsheet.toString());
        mySpreadsheet.addNewWorksheet("MartinsSpreadSheet");
        mySpreadsheet.copyWorksheet("Tabellenblatt1", "CopiedWorksheet");
        mySpreadsheet.insertValueIntoCell("Tabellenblatt2", 4, 15, "My name is Martin");
        String value = mySpreadsheet.receiveValueAtKey("Tabellenblatt2", "E11");

        int valueAsInteger ;
        try {
            valueAsInteger = Integer.parseInt(value);
            valueAsInteger++;
            value = Integer.toString(valueAsInteger);
        } catch (NumberFormatException e) {
            value = "Could not convert item";
        }
        mySpreadsheet.insertValueIntoCell("Tabellenblatt2", 5, 11, value);

        mySpreadsheet.update();
        System.out.println(mySpreadsheet.toString());
        mySpreadsheet.deleteWorksheet("MartinsSpreadSheet");
        mySpreadsheet.deleteWorksheet("CopiedWorksheet");
    }

}