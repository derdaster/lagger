using NLog;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LaggerServer.Utils
{
    public static class Log
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();

        public static void LogError(String message, Exception ex)
        {
            var text = "Tag: " + message + Environment.NewLine;

            while (ex != null)
            {
                text += String.Format("Message: {1}{0}StackTrace:{2}{0}", Environment.NewLine, ex.Message, ex.StackTrace);
                ex = ex.InnerException;
            }

            logger.Error(text);
        }

        public static void LogInfo(String message)
        {
            logger.Info(message);
        }
    }
}