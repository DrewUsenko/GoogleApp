package net.hneu.googleapp.parser;

import java.util.ArrayList;
import java.util.List;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class SuppressException extends CellProcessorAdaptor {

    public static List<SuperCsvCellProcessorException> SUPPRESSED_EXCEPTIONS = 
            new ArrayList<>();
            
    public SuppressException(CellProcessor next) {
        super(next);
    }

    @Override
    public Object execute(Object value, CsvContext context) {
        try {            
            // attempt to execute the next processor
            return next.execute(value, context);

        } catch (SuperCsvCellProcessorException e) {
            // save the exception
            CsvContext ctx = e.getCsvContext();
            CsvContext newCtx = new CsvContext(ctx.getLineNumber(), ctx.getRowNumber(), ctx.getColumnNumber());
            newCtx.setRowSource(ctx.getRowSource());            
            SUPPRESSED_EXCEPTIONS.add(
                    new SuperCsvCellProcessorException(
                            e.getMessage(), 
                            newCtx,
                            e.getProcessor()));            
            // and suppress it (null is written as "")
            return null;
        }
    }    
}
