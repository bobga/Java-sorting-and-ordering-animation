
import javafx.application.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.util.ArrayList;
import java.util.Random;

public class Animation extends Application {
    public static void main(String[] args) {
    	Application.launch(args);
    }
    
    private static final int
    	BAR_COUNT = 15,
    	MAX_BAR_HEIGHT = 30;
    
    private static final String
		COLOR_INITIAL	= "-fx-bar-fill: #888",
		COLOR_ACTIVE	= "-fx-bar-fill: #f64",
		COLOR_FINALIZED	= "-fx-bar-fill: #3cf";
    
    private static int
    	DELAY_MILLIS = 600;
    
	@Override
    public void start(Stage stage) {
    	stage.setTitle("Sorting & Ordering");
    	stage.setWidth(800);
    	stage.setHeight(600);
    	
		final BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(12));
    	
        final BarChart<String,Number> chart = new BarChart<>(new CategoryAxis(), new NumberAxis(0, MAX_BAR_HEIGHT, 0));
        chart.setLegendVisible(false);
		chart.getYAxis().setTickLabelsVisible(false);
		chart.getXAxis().setTickLabelsVisible(false);
		chart.getXAxis().setOpacity(0);
		chart.getYAxis().setOpacity(0);
		chart.setHorizontalGridLinesVisible(false);
		chart.setVerticalGridLinesVisible(false);
 
		bars = new ArrayList<Data<String, Number>>();
		final Series<String, Number> data = new Series<>();
        chart.getData().add(data);
        for (int i = 0; i < BAR_COUNT; i++) {
        	bars.add(new Data<>(Integer.toString(i+1), 1));
        	data.getData().add(bars.get(i));
        	paint(i, COLOR_INITIAL);
        }
        pane.setCenter(chart);
        
    	inputs = new FlowPane();
    	inputs.setHgap(6);
    	inputs.setVgap(6);
    	pane.setBottom(inputs);
    	
    	createButton("Randomize", this::randomize);
    	createButton("Swap Halves", this::swapHalves);
    	createButton("Reverse All",this::reverseAll);
    	createButton("Bubble Sort",this::bubbleSort);
    	createButton("Selection Sort",this::selectionSort);
    	createButton("Insertion Sort",this::insertionSort);
    	
    	
    	String delay_ms[] = {
    			"60", "300", "600", "1200"
    	};
    	
    	createLabel("Animation delay(ms)");
    	createComboBox(delay_ms);
    	
    	
		
	    
    	Platform.runLater(this::randomize);
    	
    	stage.setScene(new Scene(pane));
    	stage.show();
    }
	
    private ArrayList<Data<String, Number>> bars;
    private FlowPane inputs;
    
	private void createButton(String label, Runnable method) {
    	final Button test = new Button(label);
    	test.setOnAction(event -> new Thread(() -> {
			Platform.runLater(() -> inputs.setDisable(true));
			method.run();
			Platform.runLater(() -> inputs.setDisable(false));
		}).start());
    	inputs.getChildren().add(test);
	}
	
	private void createComboBox(String[] item) {
		final ComboBox<String> combo_box =
    			new ComboBox<String>(FXCollections
                        .observableArrayList(item)); 
    	
    	EventHandler<ActionEvent> event = 
    			new EventHandler<ActionEvent>() { 
	        public void handle(ActionEvent e) 
	        { 
	        	DELAY_MILLIS = Integer.parseInt(combo_box.getValue());
	        } 
	    };
	    
	    combo_box.getSelectionModel().select(2);
	    combo_box.setOnAction(event);	    
	    inputs.getChildren().add(combo_box);
	}
	
	private void createLabel(String label) {
		Label description_label = 
                new Label(label);
		inputs.getChildren().add(description_label);
	}
	
	// CHART ACCESSORS & MUTATORS
    
	private void mutate(int index, int value) {
    	bars.get(index).setYValue(value);
    }
    
    private int access(int index) {
    	return (int) bars.get(index).getYValue();
    }
    
    // COLOR AND ANIMATION
    
    private void paint(int index, final String style) {
    	Platform.runLater(() -> {
			bars.get(index).getNode().setStyle(style);
		});
    }
    
	private void paintAll(final String style) {
		Platform.runLater(() -> {
			for (int i = 0; i < BAR_COUNT; i++) {
				paint(i, style);
			}
		});
    }
    
    private void delay() {
		try {
			Thread.sleep(DELAY_MILLIS);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    // ALGORITHMS FOR BUTTONS
    
    private void randomize() {
    	Random random = new Random();
    	for (int i = 0; i < BAR_COUNT; i++) {
    		mutate(i, random.nextInt(MAX_BAR_HEIGHT) + 1);
    	}
    }
    
    private void swapHalves() {
    	final int half = bars.size() / 2;
    	final int offset = bars.size() % 2;
    	
    	for (int i = 0; i < half; i++) {
    		final int j = i + half + offset;
    		
    		paint(i, COLOR_ACTIVE);
    		paint(j, COLOR_ACTIVE);
    		
    		int temp = access(i);
    		mutate(i, access(j));
    		mutate(j, temp);
    		
       		delay();
    		
	    	paint(i, COLOR_FINALIZED);
	    	paint(j, COLOR_FINALIZED);
    	}
    	paintAll(COLOR_INITIAL);
    }
    
    private void reverseAll() {
    	final int half = bars.size() / 2;
    	final int last = bars.size();
    	
    	for(int i = 0; i < half; i++) {
    		final int j = last-1-i;
    		
    		paint(i, COLOR_ACTIVE);
    		paint(j, COLOR_ACTIVE);
    		
    		int temp =  access(i);
    		mutate(i, access(j));
    		mutate(j, temp);
    		
    		delay();
    		
    		paint(i, COLOR_FINALIZED);
	    	paint(j, COLOR_FINALIZED);
    		
    	}
    	paintAll(COLOR_INITIAL);
    }
    
    private void bubbleSort() {
    	final int length = bars.size();
    	int temp = 0;
    	for(int i = 0; i < length; i++ ) {
    		for(int j = 1; j < (length - i); j++) {
    			paint(i, COLOR_ACTIVE);
        		paint(j, COLOR_ACTIVE);
        		
    			if(access(j-1) > access(j)) {
    				temp = access(j-1);
    				mutate(j-1, access(j));
    				mutate(j, temp);
    			}
    			
    			delay();
    			
        		paint(i, COLOR_FINALIZED);
    	    	paint(j, COLOR_FINALIZED);
    		}
    	}
    	paintAll(COLOR_INITIAL);
    }
    
    private void selectionSort() {
    	final int length = bars.size();
    	int j = 0;
    	for(int i = 0; i < length - 1 ; i++) {
    		int min_idx = i;
    		
			paint(i, COLOR_ACTIVE);    		
			
			
    		for(j = i+1; j < length; j++) {        		    			
    			if(access(j) < access(min_idx)) {
    				min_idx = j;
    			}
    			paint(j, COLOR_ACTIVE);
    		}
    			
			int temp = access(min_idx);
			mutate(min_idx, access(i));
			mutate(i, temp);
			
			delay();
			
    		paint(i, COLOR_FINALIZED);
	    	paint(j, COLOR_FINALIZED);    		
    	}
    	paintAll(COLOR_INITIAL);
    }
    
    private void insertionSort() {
    	final int length = bars.size();
    	for(int i = 1; i < length; ++i) {
    		int key = access(i);
    		int j = i - 1;
    		
    		paint(i, COLOR_ACTIVE);

    		
    		while(j >= 0 && access(j) > key) {    			
    			mutate(j+1, access(j));
    			j = j - 1;
        		paint(j, COLOR_ACTIVE);
    		}
    		
    		mutate(j+1, key);
    		
    		delay();
			
    		paint(i, COLOR_FINALIZED);
	    	paint(j, COLOR_FINALIZED);
    	}
    	paintAll(COLOR_INITIAL);
    }
}