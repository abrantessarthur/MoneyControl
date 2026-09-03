import { useCallback, useEffect, useMemo, useState } from "react";
import {
  ArrowDownLeft,
  ArrowRight,
  ArrowUpRight,
  CalendarDays,
  ChevronDown,
  CircleDollarSign,
  CreditCard,
  Gauge,
  Layers3,
  LogOut,
  Menu,
  Pencil,
  Plus,
  ReceiptText,
  Search,
  Target,
  Trash2,
  WalletCards,
  X,
} from "lucide-react";
import {
  CartesianGrid,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

const API_URL = import.meta.env.VITE_API_URL || "/api";

const money = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

const shortDate = new Intl.DateTimeFormat("pt-BR", {
  day: "2-digit",
  month: "short",
});

const fullDate = new Intl.DateTimeFormat("pt-BR", {
  day: "2-digit",
  month: "short",
  year: "numeric",
  timeZone: "UTC",
});

const demoTransactions = [
  { id: 1, description: "Produto digital", amount: 3480, typeTransactional: "INCOME", categoryName: "Receita", date: "2026-08-29T09:00:00" },
  { id: 2, description: "Aluguel", amount: 1650, typeTransactional: "EXPENSE", categoryName: "Casa", date: "2026-08-27T08:30:00" },
  { id: 3, description: "Mercado do mês", amount: 612.84, typeTransactional: "EXPENSE", categoryName: "Alimentação", date: "2026-08-25T18:42:00" },
  { id: 4, description: "Freelance — interface", amount: 2200, typeTransactional: "INCOME", categoryName: "Trabalho", date: "2026-08-21T14:10:00" },
  { id: 5, description: "Fone de ouvido 2/6", amount: 189.9, typeTransactional: "EXPENSE", categoryName: "Tecnologia", date: "2026-08-19T12:00:00" },
  { id: 6, description: "Academia", amount: 119.9, typeTransactional: "EXPENSE", categoryName: "Saúde", date: "2026-08-15T07:20:00" },
];

const demoCategories = [
  { id: 1, name: "Alimentação" },
  { id: 2, name: "Casa" },
  { id: 3, name: "Transporte" },
  { id: 4, name: "Tecnologia" },
  { id: 5, name: "Saúde" },
];

const demoGoals = [
  { id: 1, description: "Reserva de emergência", amountToAchieve: 18000, amount: 7350, amountLeft: 10650, initialDate: "2026-06-01", finalDate: "2027-05-31" },
  { id: 2, description: "Viagem de férias", amountToAchieve: 8500, amount: 5100, amountLeft: 3400, initialDate: "2026-07-15", finalDate: "2026-12-20" },
];

const flowData = [
  { day: "01", value: 2300 }, { day: "05", value: 2840 },
  { day: "09", value: 2410 }, { day: "13", value: 3080 },
  { day: "17", value: 2760 }, { day: "21", value: 4220 },
  { day: "25", value: 3610 }, { day: "31", value: 5387 },
];

const actionLabels = {
  transaction: "Nova movimentação",
  category: "Nova categoria",
  budget: "Novo orçamento",
  installment: "Nova compra parcelada",
  card: "Novo cartão",
  goal: "Nova meta financeira",
};

async function api(path, { token, body, method = "GET" } = {}) {
  const response = await fetch(`${API_URL}${path}`, {
    method,
    headers: {
      ...(body ? { "Content-Type": "application/json" } : {}),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  });

  if (response.status === 401) throw new Error("UNAUTHORIZED");
  if (!response.ok) {
    const payload = await response.json().catch(() => null);
    throw new Error(payload?.message || `Erro ${response.status}`);
  }

  if (response.status === 204 || response.headers.get("content-length") === "0") return null;
  return response.json().catch(() => null);
}

function cx(...classes) {
  return classes.filter(Boolean).join(" ");
}

function Login({ onSession }) {
  const [mode, setMode] = useState("login");
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");
  const [form, setForm] = useState({ email: "", password: "", username: "" });

  async function submit(event) {
    event.preventDefault();
    setBusy(true);
    setError("");

    try {
      if (mode === "register") {
        await api("/auth/register", { method: "POST", body: form });
      }
      const data = await api("/auth/login", {
        method: "POST",
        body: { email: form.email, password: form.password },
      });
      localStorage.setItem("mc_token", data.token);
      localStorage.setItem("mc_email", form.email);
      onSession({ token: data.token, email: form.email, demo: false });
    } catch (reason) {
      setError(reason.message === "UNAUTHORIZED" ? "E-mail ou senha inválidos." : reason.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="login-shell">
      <section className="login-manifesto">
        <div className="brand-mark"><span>MC</span><i /></div>
        <div className="manifesto-copy">
          <p className="eyebrow">CONTROLE FINANCEIRO / 2026</p>
          <h1>Dinheiro sem<br />ruído visual.</h1>
          <p>Uma mesa de operação pessoal para acompanhar o que entra, o que sai e o que ainda precisa de decisão.</p>
        </div>
        <div className="login-index">
          <span>01</span>
          <p>Fluxo<br />Orçamento<br />Decisão</p>
        </div>
      </section>

      <section className="login-panel">
        <div className="login-form-wrap">
          <div className="login-heading">
            <span className="system-dot" />
            <p>SESSÃO SEGURA</p>
          </div>
          <h2>{mode === "login" ? "Acesse seu painel" : "Crie seu espaço"}</h2>
          <p className="muted">Use as credenciais da sua API Money Control.</p>

          <form onSubmit={submit} className="stack-form">
            {mode === "register" && (
              <label>
                <span>Nome</span>
                <input required value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} placeholder="Como quer ser chamado?" />
              </label>
            )}
            <label>
              <span>E-mail</span>
              <input required type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="voce@email.com" />
            </label>
            <label>
              <span>Senha</span>
              <input required type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} placeholder="••••••••" />
            </label>
            {error && <div className="form-error">{error}</div>}
            <button className="primary-button wide" disabled={busy}>
              {busy ? "Conectando…" : mode === "login" ? "Entrar" : "Criar e entrar"}
              <ArrowRight size={17} />
            </button>
          </form>

          <button className="text-button" onClick={() => setMode(mode === "login" ? "register" : "login")}>
            {mode === "login" ? "Ainda não tenho conta" : "Já tenho uma conta"}
          </button>
          <button className="demo-button" onClick={() => onSession({ token: null, email: "demo@moneycontrol.local", demo: true })}>
            Explorar com dados demonstrativos
          </button>
        </div>
      </section>
    </main>
  );
}

function Sidebar({ view, setView, mobileOpen, setMobileOpen, session, logout }) {
  const items = [
    ["overview", Gauge, "Visão geral", "01"],
    ["transactions", ReceiptText, "Movimentos", "02"],
    ["planning", Target, "Planejamento", "03"],
    ["cards", CreditCard, "Cartões", "04"],
  ];

  return (
    <aside className={cx("sidebar", mobileOpen && "open")}>
      <div className="sidebar-top">
        <div className="brand-mark"><span>MC</span><i /></div>
        <button className="icon-button mobile-only" onClick={() => setMobileOpen(false)} aria-label="Fechar menu"><X size={20} /></button>
      </div>
      <nav>
        {items.map(([id, Icon, label, number]) => (
          <button key={id} className={cx("nav-item", view === id && "active")} onClick={() => { setView(id); setMobileOpen(false); }}>
            <span className="nav-number">{number}</span>
            <Icon size={18} strokeWidth={1.7} />
            <span>{label}</span>
          </button>
        ))}
      </nav>
      <div className="sidebar-bottom">
        <div className="profile-chip">
          <div className="avatar">{session.email.slice(0, 2).toUpperCase()}</div>
          <div><strong>{session.demo ? "Modo demonstração" : "Conta pessoal"}</strong><span>{session.email}</span></div>
        </div>
        <button className="logout-button" onClick={logout}><LogOut size={16} /> Sair</button>
      </div>
    </aside>
  );
}

function Header({ view, onAction, openMenu, offline }) {
  const titles = {
    overview: ["Visão geral", "Seu dinheiro, em uma única superfície."],
    transactions: ["Movimentos", "Histórico, entradas e despesas."],
    planning: ["Planejamento", "Limites mensais e compromissos futuros."],
    cards: ["Cartões", "Limite, fechamento e vencimento."],
  };

  return (
    <header className="topbar">
      <div className="title-block">
        <button className="icon-button mobile-only" onClick={openMenu} aria-label="Abrir menu"><Menu size={20} /></button>
        <div><h1>{titles[view][0]}</h1><p>{titles[view][1]}</p></div>
      </div>
      <div className="header-actions">
        {offline && <span className="offline-tag">MODO DEMO</span>}
        <button className="primary-button" onClick={() => onAction("transaction")}><Plus size={17} /> Movimentação</button>
      </div>
    </header>
  );
}

function FlowTooltip({ active, payload }) {
  if (!active || !payload?.length) return null;
  return <div className="chart-tooltip"><span>Saldo projetado</span><strong>{money.format(payload[0].value)}</strong></div>;
}

function Overview({ data, onAction }) {
  const delta = data.income ? ((data.balance / data.income) * 100).toFixed(1) : "0.0";
  return (
    <div className="view-stack">
      <section className="balance-stage">
        <div className="balance-main">
          <div className="section-kicker"><span>POSIÇÃO ATUAL</span><span>BRL</span></div>
          <p className="balance-label">Saldo disponível</p>
          <h2>{money.format(data.balance)}</h2>
          <div className="balance-meta"><span className="trend"><ArrowUpRight size={16} /> {delta}% do que entrou preservado</span><span>Atualizado agora</span></div>
        </div>
        <div className="flow-panel">
          <div className="panel-head"><div><span>FLUXO DO MÊS</span><strong>Agosto</strong></div><button className="icon-button"><ChevronDown size={17} /></button></div>
          <div className="chart-wrap">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={flowData} margin={{ top: 16, right: 8, bottom: 0, left: -26 }}>
                <CartesianGrid vertical={false} stroke="#2b2b2b" strokeDasharray="2 7" />
                <XAxis dataKey="day" axisLine={false} tickLine={false} tick={{ fill: "#737373", fontSize: 10 }} />
                <YAxis axisLine={false} tickLine={false} tick={{ fill: "#737373", fontSize: 10 }} />
                <Tooltip content={<FlowTooltip />} cursor={{ stroke: "#777", strokeDasharray: "3 5" }} />
                <Line type="monotone" dataKey="value" stroke="#f2f2ed" strokeWidth={2} dot={false} activeDot={{ r: 4, fill: "#080808", stroke: "#fff" }} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>
      </section>

      <section className="ledger-strip">
        <div><span>ENTRADAS</span><strong>{money.format(data.income)}</strong><small><ArrowDownLeft size={14} /> crédito acumulado</small></div>
        <div><span>SAÍDAS</span><strong>{money.format(data.expense)}</strong><small><ArrowUpRight size={14} /> débito acumulado</small></div>
        <div><span>COMPROMETIDO</span><strong>{money.format(data.expense * 0.42)}</strong><small><Layers3 size={14} /> parcelas futuras</small></div>
        <div className="quick-action"><button onClick={() => onAction("budget")}><Target size={18} /><span>Definir orçamento</span><ArrowRight size={18} /></button></div>
      </section>

      <section className="split-section">
        <div className="recent-ledger">
          <div className="section-title"><div><span>ÚLTIMOS LANÇAMENTOS</span><h3>Movimento recente</h3></div><button className="text-link">Ver extrato <ArrowRight size={15} /></button></div>
          <TransactionRows transactions={data.transactions.slice(0, 5)} />
        </div>
        <BudgetDial expense={data.expense} onAction={onAction} />
      </section>
    </div>
  );
}

function TransactionRows({ transactions }) {
  if (!transactions.length) return <EmptyState title="Nenhuma movimentação" copy="Crie sua primeira entrada ou despesa." />;
  return (
    <div className="transaction-list">
      {transactions.map((item) => {
        const income = item.typeTransactional === "INCOME";
        return (
          <div className="transaction-row" key={item.id}>
            <div className={cx("transaction-icon", income && "income")}>{income ? <ArrowDownLeft size={18} /> : <ArrowUpRight size={18} />}</div>
            <div className="transaction-name"><strong>{item.description}</strong><span>{item.categoryName || "Sem categoria"}</span></div>
            <time>{shortDate.format(new Date(item.date))}</time>
            <strong className={cx("transaction-value", income && "income")}>{income ? "+" : "−"}{money.format(Number(item.amount))}</strong>
          </div>
        );
      })}
    </div>
  );
}

function BudgetDial({ expense, onAction }) {
  const limit = Math.max(6000, expense * 1.25);
  const used = Math.min(100, Math.round((expense / limit) * 100));
  return (
    <aside className="budget-dial">
      <div className="section-title"><div><span>ORÇAMENTO</span><h3>Ritmo de gasto</h3></div><Target size={19} /></div>
      <div className="dial" style={{ "--value": `${used * 3.6}deg` }}><div><strong>{used}%</strong><span>utilizado</span></div></div>
      <div className="budget-numbers"><span>Gasto <strong>{money.format(expense)}</strong></span><span>Limite estimado <strong>{money.format(limit)}</strong></span></div>
      <button className="outline-button wide" onClick={() => onAction("budget")}>Ajustar limite <ArrowRight size={16} /></button>
    </aside>
  );
}

function TransactionsView({ transactions, onAction, onDeleteTransaction }) {
  const [query, setQuery] = useState("");
  const [filter, setFilter] = useState("ALL");
  const filtered = transactions.filter((item) => {
    const matchQuery = item.description.toLowerCase().includes(query.toLowerCase());
    const matchType = filter === "ALL" || item.typeTransactional === filter;
    return matchQuery && matchType;
  });

  return (
    <div className="view-stack">
      <section className="toolbar-panel">
        <div className="search-field"><Search size={17} /><input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Buscar no extrato" /></div>
        <div className="segmented">
          {["ALL", "INCOME", "EXPENSE"].map((item) => <button key={item} className={filter === item ? "active" : ""} onClick={() => setFilter(item)}>{item === "ALL" ? "Todos" : item === "INCOME" ? "Entradas" : "Saídas"}</button>)}
        </div>
        <button className="outline-button" onClick={() => onAction("category")}><Plus size={16} /> Categoria</button>
      </section>
      <section className="table-shell">
        <div className="table-head"><span>DESCRIÇÃO</span><span>CATEGORIA</span><span>DATA</span><span>TIPO</span><span>VALOR</span><span>AÇÕES</span></div>
        {filtered.map((item) => (
          <div className="table-row" key={item.id}>
            <strong>{item.description}</strong><span>{item.categoryName || "—"}</span><span>{shortDate.format(new Date(item.date))}</span><span className="type-label">{item.typeTransactional === "INCOME" ? "ENTRADA" : "SAÍDA"}</span><strong className={item.typeTransactional === "INCOME" ? "positive" : ""}>{item.typeTransactional === "INCOME" ? "+" : "−"}{money.format(Number(item.amount))}</strong><button className="icon-button danger transaction-delete" onClick={() => onDeleteTransaction(item)} aria-label={`Excluir ${item.description}`}><Trash2 size={14} /></button>
          </div>
        ))}
        {!filtered.length && <EmptyState title="Nada encontrado" copy="Mude o filtro ou crie uma movimentação." />}
      </section>
    </div>
  );
}

function PlanningView({ installments, goals, onAction, onEditGoal, onDeleteGoal, expense }) {
  const items = installments.length ? installments : [
    { id: 1, description: "Notebook de trabalho", totalAmount: 5899, totalInstallments: 10, categoryId: 4 },
    { id: 2, description: "Cadeira ergonômica", totalAmount: 1740, totalInstallments: 6, categoryId: 2 },
  ];
  return (
    <div className="planning-grid">
      <section className="planning-intro">
        <div><p className="eyebrow">PRÓXIMOS 90 DIAS</p><h2>O futuro também entra na conta.</h2><p>Transforme compromissos em decisões antes que eles virem surpresa.</p></div>
        <div className="planning-actions"><button className="primary-button" onClick={() => onAction("goal")}><Plus size={16} /> Meta financeira</button><button className="outline-button" onClick={() => onAction("installment")}><Plus size={16} /> Compra parcelada</button></div>
      </section>
      <section className="goals-board">
        <div className="section-title"><div><span>METAS FINANCEIRAS</span><h3>Objetivos em andamento</h3></div><button className="icon-button" onClick={() => onAction("goal")} aria-label="Criar meta"><Plus size={18} /></button></div>
        {goals.length ? <div className="goal-grid">{goals.map((goal) => {
          const target = Number(goal.amountToAchieve);
          const current = Number(goal.amount);
          const progress = target > 0 ? Math.min(100, Math.round((current / target) * 100)) : 0;
          return <article className="goal-card" key={goal.id}>
            <div className="goal-card-head"><span>{String(progress).padStart(2, "0")}%</span><div><button className="icon-button" onClick={() => onEditGoal(goal)} aria-label={`Editar ${goal.description}`}><Pencil size={14} /></button><button className="icon-button danger" onClick={() => onDeleteGoal(goal)} aria-label={`Excluir ${goal.description}`}><Trash2 size={14} /></button></div></div>
            <h4>{goal.description}</h4>
            <div className="goal-progress"><i style={{ width: `${progress}%` }} /></div>
            <div className="goal-values"><span>Guardado<strong>{money.format(current)}</strong></span><span>Objetivo<strong>{money.format(target)}</strong></span></div>
            <div className="goal-deadline"><CalendarDays size={14} /><span>Prazo</span><strong>{fullDate.format(new Date(`${goal.finalDate}T00:00:00Z`))}</strong></div>
          </article>;
        })}</div> : <EmptyState title="Nenhuma meta financeira" copy="Crie um objetivo e acompanhe seu progresso." />}
      </section>
      <section className="commitment-board">
        <div className="section-title"><div><span>PARCELAMENTOS ATIVOS</span><h3>Compromissos</h3></div><strong>{String(items.length).padStart(2, "0")}</strong></div>
        {items.map((item, index) => {
          const paid = Math.min(item.totalInstallments - 1, index + 2);
          return <article className="commitment" key={item.id}><div className="commitment-index">{String(index + 1).padStart(2, "0")}</div><div className="commitment-copy"><strong>{item.description}</strong><span>{money.format(Number(item.totalAmount))} em {item.totalInstallments}x</span><div className="mini-progress"><i style={{ width: `${(paid / item.totalInstallments) * 100}%` }} /></div></div><div className="commitment-status"><strong>{paid}/{item.totalInstallments}</strong><span>parcelas</span></div></article>;
        })}
      </section>
      <section className="budget-board">
        <div className="section-title"><div><span>ORÇAMENTO DO MÊS</span><h3>Limites por categoria</h3></div><button className="icon-button" onClick={() => onAction("budget")}><Plus size={18} /></button></div>
        {[{ name: "Casa", value: expense * .43, limit: 2600 }, { name: "Alimentação", value: expense * .2, limit: 1200 }, { name: "Transporte", value: expense * .12, limit: 800 }].map((row) => <div className="budget-line" key={row.name}><div><strong>{row.name}</strong><span>{money.format(row.value)} de {money.format(row.limit)}</span></div><div className="linear-progress"><i style={{ width: `${Math.min(100, row.value / row.limit * 100)}%` }} /></div></div>)}
      </section>
    </div>
  );
}

function CardsView({ onAction }) {
  return (
    <div className="cards-layout">
      <section className="credit-card-stage">
        <div className="physical-card">
          <div className="card-top"><span>MONEY / CREDIT</span><span>●●</span></div>
          <div className="card-chip"><i /><i /><i /></div>
          <div className="card-number">•••• &nbsp;•••• &nbsp;•••• &nbsp;4821</div>
          <div className="card-footer"><div><span>TITULAR</span><strong>CONTA PESSOAL</strong></div><div><span>VENCE</span><strong>17</strong></div></div>
        </div>
        <button className="outline-button" onClick={() => onAction("card")}><Plus size={16} /> Cadastrar cartão</button>
      </section>
      <section className="invoice-panel">
        <div className="section-kicker"><span>FATURA ATUAL</span><span>FECHA EM 08 DIAS</span></div>
        <p>Valor acumulado</p><h2>{money.format(1948.72)}</h2>
        <div className="invoice-scale"><i style={{ width: "39%" }} /></div>
        <div className="invoice-stats"><span>Limite utilizado <strong>39%</strong></span><span>Disponível <strong>{money.format(3051.28)}</strong></span></div>
        <div className="invoice-list"><div><CalendarDays size={17} /><span>Melhor dia de compra</span><strong>11 SET</strong></div><div><CircleDollarSign size={17} /><span>Vencimento</span><strong>17 SET</strong></div></div>
      </section>
    </div>
  );
}

function EmptyState({ title, copy }) {
  return <div className="empty-state"><WalletCards size={26} /><strong>{title}</strong><span>{copy}</span></div>;
}

function ActionSheet({ action, categories, initialGoal, onClose, onSubmit, onDeleteCategory, busy }) {
  const today = new Date().toISOString().slice(0, 10);
  const month = today.slice(0, 7);
  const [form, setForm] = useState({ description: initialGoal?.description || "", amount: initialGoal?.amount ?? "", amountToAchieve: initialGoal?.amountToAchieve ?? "", finalDate: initialGoal?.finalDate || today, typeTransactional: "EXPENSE", categoryId: categories[0]?.id || "", date: `${today}T12:00`, name: "", limitAmount: "", month, totalAmount: "", totalInstallments: 2, firstDueDate: today, lastFourDigits: "", creditLimit: "", closingDay: 10, dueDay: 17 });

  function send(event) {
    event.preventDefault();
    const payloads = {
      transaction: { description: form.description, amount: Number(form.amount), typeTransactional: form.typeTransactional, categoryId: Number(form.categoryId), date: form.date },
      category: { name: form.name },
      budget: { limitAmount: Number(form.limitAmount), month: form.month, categoryId: Number(form.categoryId) },
      installment: { description: form.description, totalAmount: Number(form.totalAmount), totalInstallments: Number(form.totalInstallments), firstDueDate: form.firstDueDate, categoryId: Number(form.categoryId) },
      card: { name: form.name, lastFourDigits: form.lastFourDigits, creditLimit: Number(form.creditLimit), closingDay: Number(form.closingDay), dueDay: Number(form.dueDay) },
      goal: { description: form.description, amountToAchieve: Number(form.amountToAchieve), amount: Number(form.amount), finalDate: form.finalDate },
    };
    onSubmit(action, payloads[action]);
  }

  return (
    <div className="sheet-backdrop" onMouseDown={(e) => e.target === e.currentTarget && onClose()}>
      <aside className="action-sheet">
        <div className="sheet-head"><div><span>{initialGoal ? "EDITAR REGISTRO" : "NOVO REGISTRO"}</span><h2>{initialGoal ? "Editar meta financeira" : actionLabels[action]}</h2></div><button className="icon-button" onClick={onClose}><X size={20} /></button></div>
        <form className="stack-form" onSubmit={send}>
          {(action === "transaction" || action === "installment" || action === "goal") && <label><span>Descrição</span><input required value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} placeholder={action === "goal" ? "Ex.: Reserva de emergência" : "Ex.: Mercado, notebook, salário"} /></label>}
          {(action === "category" || action === "card") && <label><span>{action === "card" ? "Nome do cartão" : "Nome da categoria"}</span><input required value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} placeholder={action === "card" ? "Ex.: Nubank principal" : "Ex.: Alimentação"} /></label>}
          {action === "transaction" && <><div className="form-grid"><label><span>Valor</span><input required type="number" min="0.01" step="0.01" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} placeholder="0,00" /></label><label><span>Tipo</span><select value={form.typeTransactional} onChange={(e) => setForm({ ...form, typeTransactional: e.target.value })}><option value="EXPENSE">Saída</option><option value="INCOME">Entrada</option></select></label></div><label><span>Data e hora</span><input type="datetime-local" value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })} /></label></>}
          {action === "budget" && <div className="form-grid"><label><span>Limite mensal</span><input required type="number" min="0.01" step="0.01" value={form.limitAmount} onChange={(e) => setForm({ ...form, limitAmount: e.target.value })} /></label><label><span>Mês</span><input required type="month" value={form.month} onChange={(e) => setForm({ ...form, month: e.target.value })} /></label></div>}
          {action === "installment" && <><div className="form-grid"><label><span>Valor total</span><input required type="number" min="0.01" step="0.01" value={form.totalAmount} onChange={(e) => setForm({ ...form, totalAmount: e.target.value })} /></label><label><span>Parcelas</span><input required type="number" min="2" value={form.totalInstallments} onChange={(e) => setForm({ ...form, totalInstallments: e.target.value })} /></label></div><label><span>Primeiro vencimento</span><input required type="date" value={form.firstDueDate} onChange={(e) => setForm({ ...form, firstDueDate: e.target.value })} /></label></>}
          {action === "card" && <><div className="form-grid"><label><span>Últimos 4 dígitos</span><input required inputMode="numeric" maxLength="4" value={form.lastFourDigits} onChange={(e) => setForm({ ...form, lastFourDigits: e.target.value.replace(/\D/g, "") })} placeholder="4821" /></label><label><span>Limite</span><input required type="number" min="0.01" step="0.01" value={form.creditLimit} onChange={(e) => setForm({ ...form, creditLimit: e.target.value })} /></label></div><div className="form-grid"><label><span>Dia de fechamento</span><input required type="number" min="1" max="31" value={form.closingDay} onChange={(e) => setForm({ ...form, closingDay: e.target.value })} /></label><label><span>Dia de vencimento</span><input required type="number" min="1" max="31" value={form.dueDay} onChange={(e) => setForm({ ...form, dueDay: e.target.value })} /></label></div></>}
          {action === "goal" && <><div className="form-grid"><label><span>Valor atual</span><input required type="number" min="0" step="0.01" value={form.amount} onChange={(e) => setForm({ ...form, amount: e.target.value })} /></label><label><span>Valor objetivo</span><input required type="number" min="0.01" step="0.01" value={form.amountToAchieve} onChange={(e) => setForm({ ...form, amountToAchieve: e.target.value })} /></label></div><label><span>Data final</span><input required type="date" min={today} value={form.finalDate} onChange={(e) => setForm({ ...form, finalDate: e.target.value })} /></label></>}
          {(action === "transaction" || action === "budget" || action === "installment") && <label><span>Categoria</span><select required value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })}><option value="" disabled>Selecione</option>{categories.map((category) => <option key={category.id} value={category.id}>{category.name}</option>)}</select></label>}
          {action === "category" && <section className="category-manager">
            <div className="category-manager-head"><span>CATEGORIAS ATUAIS</span><strong>{String(categories.length).padStart(2, "0")}</strong></div>
            {categories.length ? <div className="category-manager-list">{categories.map((category) => <div className="category-manager-row" key={category.id}>
              <span>{category.name}</span>
              <button type="button" className="icon-button danger" onClick={() => onDeleteCategory(category)} disabled={busy} aria-label={`Excluir ${category.name}`}><Trash2 size={14} /></button>
            </div>)}</div> : <p className="category-manager-empty">Nenhuma categoria cadastrada.</p>}
          </section>}
          <div className="sheet-note"><span>INFO</span><p>{action === "installment" ? "As parcelas serão lançadas automaticamente como despesas mensais." : action === "goal" ? "O valor atual não pode ultrapassar o valor objetivo." : "O registro será enviado diretamente para sua API Money Control."}</p></div>
          <button className="primary-button wide" disabled={busy}>{busy ? "Salvando…" : "Confirmar registro"}<ArrowRight size={17} /></button>
        </form>
      </aside>
    </div>
  );
}

export default function App() {
  const storedToken = localStorage.getItem("mc_token");
  const [session, setSession] = useState(storedToken ? { token: storedToken, email: localStorage.getItem("mc_email") || "usuario@moneycontrol", demo: false } : null);
  const [view, setView] = useState("overview");
  const [mobileOpen, setMobileOpen] = useState(false);
  const [action, setAction] = useState(null);
  const [editingGoal, setEditingGoal] = useState(null);
  const [busy, setBusy] = useState(false);
  const [notice, setNotice] = useState("");
  const [data, setData] = useState({ balance: 0, income: 0, expense: 0, transactions: [], categories: [], installments: [], goals: [] });

  const demoData = useMemo(() => ({ balance: 5387.36, income: 10420, expense: 5032.64, transactions: demoTransactions, categories: demoCategories, installments: [], goals: demoGoals }), []);

  const load = useCallback(async () => {
    if (!session) return;
    if (session.demo) { setData(demoData); return; }
    try {
      const [balance, summary, transactions, categories, installments, goals] = await Promise.all([
        api("/transactions/balance", { token: session.token }),
        api("/transactions/sumary", { token: session.token }),
        api("/transactions/page/0/size/20", { token: session.token }),
        api("/categorys/page/0/size/50", { token: session.token }),
        api("/installments/page/0/size/10", { token: session.token }),
        api("/goals", { token: session.token }),
      ]);
      setData({ balance: Number(balance || 0), income: Number(summary?.totalIncome || 0), expense: Number(summary?.totalExpense || 0), transactions: transactions?.content || [], categories: categories?.content || [], installments: installments?.content || [], goals: goals || [] });
    } catch (reason) {
      if (reason.message === "UNAUTHORIZED") {
        localStorage.removeItem("mc_token");
        setSession(null);
      } else {
        setNotice(`API indisponível: ${reason.message}`);
      }
    }
  }, [session, demoData]);

  useEffect(() => { load(); }, [load]);
  useEffect(() => { if (!notice) return; const id = setTimeout(() => setNotice(""), 3500); return () => clearTimeout(id); }, [notice]);

  function logout() {
    localStorage.removeItem("mc_token");
    localStorage.removeItem("mc_email");
    setSession(null);
  }

  async function submitAction(type, body) {
    if (session.demo) { setNotice("No modo demo, os registros não são enviados."); setAction(null); return; }
    const routes = { transaction: "/transactions", category: "/categorys", budget: "/budget", installment: "/installments", card: "/credit", goal: "/goals" };
    setBusy(true);
    try {
      const goalUpdate = type === "goal" && editingGoal;
      await api(goalUpdate ? `/goals/${editingGoal.id}` : routes[type], { token: session.token, method: goalUpdate ? "PUT" : "POST", body });
      setNotice("Registro salvo com sucesso.");
      setAction(null);
      setEditingGoal(null);
      await load();
    } catch (reason) {
      setNotice(reason.message === "UNAUTHORIZED" ? "Sua sessão expirou. Entre novamente." : reason.message);
      if (reason.message === "UNAUTHORIZED") logout();
    } finally {
      setBusy(false);
    }
  }

  function editGoal(goal) {
    setEditingGoal(goal);
    setAction("goal");
  }

  async function deleteGoal(goal) {
    if (session.demo) { setNotice("No modo demo, as metas não são alteradas."); return; }
    if (!window.confirm(`Excluir a meta “${goal.description}”?`)) return;
    setBusy(true);
    try {
      await api(`/goals/${goal.id}`, { token: session.token, method: "DELETE" });
      setNotice("Meta excluída com sucesso.");
      await load();
    } catch (reason) {
      setNotice(reason.message === "UNAUTHORIZED" ? "Sua sessão expirou. Entre novamente." : reason.message);
      if (reason.message === "UNAUTHORIZED") logout();
    } finally {
      setBusy(false);
    }
  }

  async function deleteCategory(category) {
    if (session.demo) { setNotice("No modo demo, as categorias não são alteradas."); return; }
    if (!window.confirm(`Excluir a categoria “${category.name}”?`)) return;
    setBusy(true);
    try {
      await api(`/categorys/${category.id}`, { token: session.token, method: "DELETE" });
      setNotice("Categoria excluída com sucesso.");
      await load();
    } catch (reason) {
      setNotice(reason.message === "UNAUTHORIZED" ? "Sua sessão expirou. Entre novamente." : reason.message);
      if (reason.message === "UNAUTHORIZED") logout();
    } finally {
      setBusy(false);
    }
  }

  async function deleteTransaction(transaction) {
    if (session.demo) { setNotice("No modo demo, as movimentações não são alteradas."); return; }
    if (!window.confirm(`Excluir a movimentação “${transaction.description}”?`)) return;
    setBusy(true);
    try {
      await api(`/transactions/${transaction.id}`, { token: session.token, method: "DELETE" });
      setNotice("Movimentação excluída com sucesso.");
      await load();
    } catch (reason) {
      setNotice(reason.message === "UNAUTHORIZED" ? "Sua sessão expirou. Entre novamente." : reason.message);
      if (reason.message === "UNAUTHORIZED") logout();
    } finally {
      setBusy(false);
    }
  }

  if (!session) return <Login onSession={setSession} />;

  return (
    <div className="app-shell">
      <Sidebar view={view} setView={setView} mobileOpen={mobileOpen} setMobileOpen={setMobileOpen} session={session} logout={logout} />
      {mobileOpen && <div className="mobile-scrim" onClick={() => setMobileOpen(false)} />}
      <div className="workspace">
        <Header view={view} onAction={setAction} openMenu={() => setMobileOpen(true)} offline={session.demo} />
        <main className="content">
          {view === "overview" && <Overview data={data} onAction={setAction} />}
          {view === "transactions" && <TransactionsView transactions={data.transactions} onAction={setAction} onDeleteTransaction={deleteTransaction} />}
          {view === "planning" && <PlanningView installments={data.installments} goals={data.goals} expense={data.expense} onAction={(next) => { setEditingGoal(null); setAction(next); }} onEditGoal={editGoal} onDeleteGoal={deleteGoal} />}
          {view === "cards" && <CardsView onAction={setAction} />}
        </main>
      </div>
      {action && <ActionSheet action={action} categories={data.categories} initialGoal={editingGoal} onClose={() => { setAction(null); setEditingGoal(null); }} onSubmit={submitAction} onDeleteCategory={deleteCategory} busy={busy} />}
      {notice && <div className="toast"><span />{notice}</div>}
    </div>
  );
}
