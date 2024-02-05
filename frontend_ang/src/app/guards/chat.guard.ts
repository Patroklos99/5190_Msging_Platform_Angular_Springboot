import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {LoginService} from "../login/login.service";

export const chatGuard: CanActivateFn = (route, state) => {
  const loginService = inject(LoginService)
  const router = inject(Router)
  if (loginService.getToken() === null) {
    return router.parseUrl('/')
  }
  return true;
};
