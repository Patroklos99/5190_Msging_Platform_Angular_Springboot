import {Injectable} from '@angular/core';
import {
	HttpInterceptor,
	HttpEvent,
	HttpRequest,
	HttpHandler, HTTP_INTERCEPTORS,
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {LoginService} from './login.service';

@Injectable()
export class MyHttpInterceptor implements HttpInterceptor{
	constructor(private loginService : LoginService) {
	}

	intercept(req: HttpRequest<any>, next: HttpHandler):
		Observable<HttpEvent<any>> {
		const token = this.loginService.getToken();
		const modifiedReq = req.clone({
			url: req.url,
			headers: req.headers.set('Authorization', `Bearer ${token}`),
		});

		return next.handle(modifiedReq);
	}
}