import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptors, withInterceptorsFromDi} from "@angular/common/http";
import {MyHttpInterceptor} from "./login/auth.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [
      provideRouter(routes),
      provideHttpClient(),
      { provide : HTTP_INTERCEPTORS, useClass : MyHttpInterceptor, multi : true },
  ],
};
