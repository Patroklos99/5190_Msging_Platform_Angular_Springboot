import {ComponentFixture, TestBed} from "@angular/core/testing";
import {ReactiveFormsModule} from "@angular/forms";
import {TestHelper} from "../../test/test-helper";
import {LoginFormComponent} from "./login-form.component";
import {HttpClientModule} from "@angular/common/http";

describe("LoginFormComponent", () => {
	let component: LoginFormComponent;
	let fixture: ComponentFixture<LoginFormComponent>;
	let testHelper: TestHelper<LoginFormComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			declarations: [LoginFormComponent],
			imports: [ReactiveFormsModule, HttpClientModule],
		}).compileComponents();

		fixture = TestBed.createComponent(LoginFormComponent);
		component = fixture.componentInstance;
		testHelper = new TestHelper(fixture);
		fixture.detectChanges();
	});

	it("should create", () => {
		let username: string;
		let password: string;

		// On s'abonne à l'EventEmitter pour recevoir les valeurs émises.
		component.login.subscribe((event) => {
			username = event.username;
			password = event.password;
		});

		// À compléter
		const usernameinput = testHelper.getInput('username')
		testHelper.writeInInput(usernameinput, 'username')
		const passinput = testHelper.getInput('password')
		testHelper.writeInInput(passinput, 'pwd')
		const button = testHelper.getButton('btn-login')

		fixture.detectChanges();
		button.click();

		expect(username!).toBe('username');
		expect(password!).toBe('pwd');
		expect(component.loginForm.valid).toBe(true);
	});

	it("username absent", () => {
		let username: string;
		let password: string;

		// On s'abonne à l'EventEmitter pour recevoir les valeurs émises.
		component.login.subscribe((event) => {
			username = event.username;
			password = event.password;
		});

		// À compléter
		const usernameinput = testHelper.getInput('username')
		testHelper.writeInInput(usernameinput, '')
		const passinput = testHelper.getInput('password')
		testHelper.writeInInput(passinput, 'pwd')
		const button = testHelper.getButton('btn-login')

		fixture.detectChanges();
		button.click();

		// expect(username!).toBe('username');
		expect(username!).toBeUndefined();
		expect(password!).toBe('pwd');
		expect(component.loginForm.valid).toBe(false);
	});


	it("password absent", () => {
		let username: string;
		let password: string;

		// On s'abonne à l'EventEmitter pour recevoir les valeurs émises.
		component.login.subscribe((event) => {
			username = event.username;
			password = event.password;
		});

		// À compléter
		const usernameinput = testHelper.getInput('username')
		testHelper.writeInInput(usernameinput, 'username')
		const passinput = testHelper.getInput('password')
		testHelper.writeInInput(passinput, '')
		const button = testHelper.getButton('btn-login')

		fixture.detectChanges();
		button.click();

		expect(username!).toBe('username');
		expect(password!).toBeUndefined()
		expect(component.loginForm.valid).toBe(false);
	});

	it("username and password absent", () => {
		let username: string;
		let password: string;

		// On s'abonne à l'EventEmitter pour recevoir les valeurs émises.
		component.login.subscribe((event) => {
			username = event.username;
			password = event.password;
		});

		// À compléter
		const usernameinput = testHelper.getInput('username')
		testHelper.writeInInput(usernameinput, '')
		const passinput = testHelper.getInput('password')
		testHelper.writeInInput(passinput, '')
		const button = testHelper.getButton('btn-login')

		fixture.detectChanges();
		button.click();

		expect(username!).toBeUndefined()
		expect(password!).toBeUndefined()
		expect(component.loginForm.valid).toBe(false);
	});
});
