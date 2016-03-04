package work.billing.Export;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import work.billing.Timesheet.TrackedTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mhillbrand on 3/4/2016.
 */
public class TestInternalProjectMatrix {
    private InternalProjectMatrix internalPrjMatrix;
    private static List<TrackedTime> trackedTimes;
    public static final String PROJECT_NAME = "TestName";

    @BeforeClass
    public static void SetUpClass() {
        trackedTimes = new ArrayList<>();
        trackedTimes.add(new TrackedTime(PROJECT_NAME, "2016-01", "Martin Hillbrand", 5.5, 10.5, 55.9, 90));
        trackedTimes.add(new TrackedTime(PROJECT_NAME, "2016-01", "Test User", 6.5, 16.5, 56.9, 60));
        trackedTimes.add(new TrackedTime(PROJECT_NAME, "2016-01", "Another TestUser", 7.5, 17.5, 57.9, 70));
    }
    @Before
    public void Setup() {
        internalPrjMatrix = new InternalProjectMatrix(PROJECT_NAME, trackedTimes);
    }

    @Test
    public void PutHeadingToMatrix_WithStartPos5_ReturnsCorrectHeadingOrder() {
        int row = 5;
        internalPrjMatrix.putHeadingToMatrix(row);

        Assert.assertEquals(PROJECT_NAME, internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.A.ordinal()).get(row));
        Assert.assertEquals(I18N.HOUR_RATE, internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.C.ordinal()).get(row));
        Assert.assertEquals(I18N.NET, internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.D.ordinal()).get(row));
        Assert.assertEquals(I18N.VAT, internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.E.ordinal()).get(row));
        Assert.assertEquals(I18N.PRE_TAX, internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.F.ordinal()).get(row));
        Assert.assertEquals(row, internalPrjMatrix.getHeadingRow());
    }

    @Test
    public void PutHeadingToMatrix_WithStartPos5_ReturnsCorrectColumnSizeOf5() {
        int row = 5;
        internalPrjMatrix.putHeadingToMatrix(row);
        Assert.assertEquals(5, internalPrjMatrix.cellMatrix.keySet().size());
    }

    @Test
    public void PutVATtoMatrix_WithStartPos5_ReturnsCorrectVATANDSUMField() {
        internalPrjMatrix.putVATtoMatrix(5);

        Assert.assertEquals("=D5*0.20", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.E.ordinal()).get(5));
        Assert.assertEquals("=D5+E5", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.F.ordinal()).get(5));
    }

    @Test
    public void PutSumLineToMatrix_WithStartPos5_ReturnsSumFormularInD_E_F() {
        int row = 5;
        internalPrjMatrix.putSumLineToMatrix(row, row-3);

        Assert.assertEquals("=SUM(D2:D4)", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.D.ordinal()).get(row));
        Assert.assertEquals("=SUM(E2:E4)", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.E.ordinal()).get(row));
        Assert.assertEquals("=SUM(F2:F4)", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.F.ordinal()).get(row));
    }

    @Test
    public void PutTravelCostsToMatrix_WithStartPos5_ReturnsTravelCostsAt_B_D() {
        int row = 5;
        internalPrjMatrix.putTravelCostsToMatrix(row, 22.7);

        Assert.assertEquals(I18N.TRAVEL_COSTS, internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.B.ordinal()).get(row));
        Assert.assertEquals("22.70", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.D.ordinal()).get(row));
    }

    @Test
    public void PutTimeForTeamMemberToMatrix_WithStartPos5_RetrunsTeamMemberTimesOfMartinHillbrand(){
        int row = 5;
        internalPrjMatrix.putTimeForTeamMemberToMatrix(row, trackedTimes.get(0));

        Assert.assertEquals("5.50", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.A.ordinal()).get(row));
        Assert.assertEquals("Martin Hillbrand", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.B.ordinal()).get(row));
        Assert.assertEquals("90", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.C.ordinal()).get(row));
        Assert.assertEquals("=A5*C5", internalPrjMatrix.cellMatrix.get(BaseSpreadSheetMatrix.COL.D.ordinal()).get(row));
    }

    @Test
    public void InitializeMatrix_WithRow5_ReturnsSumRowAt10() {
        internalPrjMatrix.initializeMatrix(5);

        Assert.assertEquals(10, internalPrjMatrix.getSumRow());
    }

    @Test
    public void InitializeMatrix_WithRow5_ReturnsHeadingRowAt5() {
        internalPrjMatrix.initializeMatrix(5);

        Assert.assertEquals(5, internalPrjMatrix.getHeadingRow());
    }

    @Test
    public void InitializeMatrix_WithRow5_ReturnsTravelCostRowAt9() {
        internalPrjMatrix.initializeMatrix(5);

        Assert.assertEquals(9, internalPrjMatrix.getTravelCostRow());
    }

    @Test
    public void InitializeMatrix_WithRow5_ReturnsCorrectValuesAtColB() {
        internalPrjMatrix.initializeMatrix(5);

        HashMap<Integer, HashMap<Integer, String>> expected = new HashMap<>();
        int colB = BaseSpreadSheetMatrix.COL.B.ordinal();
        expected.put(colB, new HashMap<>());
        expected.get(colB).put(6, trackedTimes.get(0).getTeamMember());
        expected.get(colB).put(7, trackedTimes.get(1).getTeamMember());
        expected.get(colB).put(8, trackedTimes.get(2).getTeamMember());
        expected.get(colB).put(9, I18N.TRAVEL_COSTS);


        Assert.assertEquals(expected.get(colB), internalPrjMatrix.cellMatrix.get(colB));
    }
}
