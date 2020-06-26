
public class SemaphoreI {
	
	String type;
	Boolean available;
	
	
	public SemaphoreI(String type) {
		this.type = type;
		this.available = true;
	}
	
	public void changeAvailability() {
		this.available = !this.available;
		System.out.println("Semaphore of type " + this.type + " Availability is now " + this.available);
	}
	
	public static void main(String[] args) {
		SemaphoreI s1 = new SemaphoreI("Printing");
	}

}
