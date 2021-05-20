package club.hardcoreminecraft.javase.secureop;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

public class Log4JFilter implements Filter {
	
	/**
	 * Class borrowed from
	 * https://www.spigotmc.org/threads/how-to-disable-console-logs-for-certain-commands.350656/
	 * */

	   public Result filter(LogEvent record) {
		      try {
		         if(record != null && record.getMessage() != null) {
		            String npe = record.getMessage().getFormattedMessage().toLowerCase();
		            return !npe.contains("issued server command:")?Result.NEUTRAL:(!npe.contains("/op ") && !npe.contains("/deop ") && !npe.contains("/setoppassword ")?Result.NEUTRAL:Result.DENY);
		         } else {
		            return Result.NEUTRAL;
		         }
		      } catch (NullPointerException var3) {
		         return Result.NEUTRAL;
		      }
		   }

		   public Result filter(Logger arg0, Level arg1, Marker arg2, String message, Object... arg4) {
		      try {
		         if(message == null) {
		            return Result.NEUTRAL;
		         } else {
		            String npe = message.toLowerCase();
		            return !npe.contains("issued server command:")?Result.NEUTRAL:(!npe.contains("/op ") && !npe.contains("/deop ") && !npe.contains("/setoppassword ")?Result.NEUTRAL:Result.DENY);		         }
		      } catch (NullPointerException var7) {
		         return Result.NEUTRAL;
		      }
		   }

		   public Result filter(Logger arg0, Level arg1, Marker arg2, Object message, Throwable arg4) {
		      try {
		         if(message == null) {
		            return Result.NEUTRAL;
		         } else {
		            String npe = message.toString().toLowerCase();
		            return !npe.contains("issued server command:")?Result.NEUTRAL:(!npe.contains("/op ") && !npe.contains("/deop ") && !npe.contains("/setoppassword ")?Result.NEUTRAL:Result.DENY);		         }
		      } catch (NullPointerException var7) {
		         return Result.NEUTRAL;
		      }
		   }

		   public Result filter(Logger arg0, Level arg1, Marker arg2, Message message, Throwable arg4) {
		      try {
		         if(message == null) {
		            return Result.NEUTRAL;
		         } else {
		            String npe = message.getFormattedMessage().toLowerCase();
		            return !npe.contains("issued server command:")?Result.NEUTRAL:(!npe.contains("/op ") && !npe.contains("/deop ") && !npe.contains("/setoppassword ")?Result.NEUTRAL:Result.DENY);		         }
		      } catch (NullPointerException var7) {
		         return Result.NEUTRAL;
		      }
		   }

		   public Result getOnMatch() {
		      return Result.NEUTRAL;
		   }

		   public Result getOnMismatch() {
		      return Result.NEUTRAL;
		   }

		@Override
		public State getState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void initialize() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isStarted() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isStopped() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void start() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void stop() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
				Object arg7) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
				Object arg7, Object arg8) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
				Object arg7, Object arg8, Object arg9) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
				Object arg7, Object arg8, Object arg9, Object arg10) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
				Object arg7, Object arg8, Object arg9, Object arg10, Object arg11) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
				Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object arg4, Object arg5, Object arg6,
				Object arg7, Object arg8, Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) {
			// TODO Auto-generated method stub
			return null;
		}
		}

