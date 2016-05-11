package slidelauch;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.marknguyen.babygenderpredictor.R;


public class MyWidgetProvider extends AppWidgetProvider {
	private static final String TAG = MyWidgetProvider.class.getSimpleName();
	public static String WIDGET_BUTTON = "MY_PACKAGE_NAME.WIDGET_BUTTON";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		RemoteViews remoteViews;
		ComponentName watchWidget;

		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		watchWidget = new ComponentName(context, MyWidgetProvider.class);

		Intent intent = new Intent(WIDGET_BUTTON);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.btn_widget, pendingIntent );
		
		appWidgetManager.updateAppWidget(watchWidget, remoteViews);
	
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		 if (WIDGET_BUTTON.equals(intent.getAction())) {
			 Log.d(TAG,"Provider");
 	     }
	}
	
}
