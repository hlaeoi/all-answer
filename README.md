# 1
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Event {
	
	private EventType type;
	
	public Event(EventType type) {
		this.type = type;
	}
	
	public enum EventType {
		type1,type2,type3,type4;
	}
	
	protected abstract void handle();
	
	public interface EventListener {
		
		public void handle(EventType type);
		
	}
	
	public class EventSourse {
		
		private List<EventListener> listeners = new CopyOnWriteArrayList<>();
		
		public void handle(Event event) {
			final EventType curType = event.getType();
			for (EventListener listener : listeners) {
				listener.handle(curType);
			}
			event.handle();
		}
		
		public void addListener(EventListener listener) {
			listeners.add(listener);
		}
		
	}

	public EventType getType() {
		return type;
	}

}
#2
1.connector监听端口，获取用户请求
2.connector将请求交给engine处理
3.engine通过请求主机名匹配对应host
4.host再通过路径匹配到对应context
5.context根据文件名匹配到对应servlet
#3

#4
